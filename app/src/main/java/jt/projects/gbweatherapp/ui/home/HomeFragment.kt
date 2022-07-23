package jt.projects.gbweatherapp.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.gbweatherapp.BaseActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentHomeBinding
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.OperationType
import jt.projects.gbweatherapp.ui.OnItemViewClickListener
import jt.projects.gbweatherapp.utils.*
import jt.projects.gbweatherapp.viewmodel.AppState
import jt.projects.gbweatherapp.viewmodel.SharedPref
import java.io.IOException
import java.util.*
import kotlin.system.measureTimeMillis


private const val REFRESH_PERIOD_GPS = 1000L//периодичность запроса местоположения
private const val REFRESH_PERIOD_NETWORK = 5000L//периодичность запроса местоположения
private const val MINIMAL_DISTANCE = 0f//ли при перемещении телефона на __ метров

class HomeFragment : Fragment() {
    private val TAG = "@@@"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private var locationManager: LocationManager? = null

    companion object {
        fun newInstance() = HomeFragment()
    }


    private val adapter = HomeFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            requireActivity().showWeatherDetailsFragment(weather)
        }

        override fun onButtonFavoritesClick(
            weather: Weather,
            operation: OperationType
        ) {
            when (operation) {
                OperationType.ADD -> {
                    viewModel.addToFavorites(weather)
                    binding.root.showSnackBarShort(weather.city.name.plus(" добавлен в Избранное"))
                }
                OperationType.DELETE -> {
                    viewModel.deleteFromFavorites(weather)
                    binding.root.showSnackBarShort(weather.city.name.plus(" удален из Избранного"))
                }
            }
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true) // эта строчка говорит о том, что у фрагмента должен быть доступ к меню Активити
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.subtitle = "Список городов"
        }
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        val observer =
            Observer<AppState<List<Weather>>> { renderData(it) }// it = конкрeтный экзмепляр AppState

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java].also {
            it.getLiveData().observe(viewLifecycleOwner, observer)
            it.getCityList(SharedPref.getData().isDataSetRus)
        }
        renderDataSetButton()
        binding.buttonLocation.setOnClickListener {
            //checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            (requireActivity() as BaseActivity).tryJob(
                Manifest.permission.ACCESS_FINE_LOCATION,
                { getLocation() },
                "Запрос местоположения",
                "Требуется для отображения погоды в Вашем городе"
            )
        }
    }

    private fun initRecyclerView() {
        binding.mainFragmentRecyclerView.adapter = adapter
        //  разделитель карточек
        DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).also {
            it.setDrawable(resources.getDrawable(R.drawable.separator, null))
            binding.mainFragmentRecyclerView.addItemDecoration(it)
        }

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        val animator = DefaultItemAnimator().apply {
            addDuration = DURATION_ITEM_ANIMATOR
            changeDuration = DURATION_ITEM_ANIMATOR
            removeDuration = DURATION_ITEM_ANIMATOR
            moveDuration = DURATION_ITEM_ANIMATOR
        }
        binding.mainFragmentRecyclerView.itemAnimator = animator
    }


    private fun changeWeatherDataSet() {
        SharedPref.settings.isDataSetRus = !SharedPref.settings.isDataSetRus
        SharedPref.save()
        viewModel.getCityList(SharedPref.getData().isDataSetRus)
        renderDataSetButton()
    }


    private fun renderDataSetButton() {
        if (SharedPref.getData().isDataSetRus) {
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
    }


    private fun renderData(appState: AppState<List<Weather>>) {
        when (appState) {
            is AppState.Success<*> -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.data as List<Weather>)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(listOf())
                val action =
                    fun() { viewModel.getCityList(SharedPref.getData().isDataSetRus) }
                binding.root.showSnackBarWithAction(
                    appState.error.message ?: "",
                    R.string.reload,
                    action
                )
            }
        }
    }

    override fun onDestroy() {
        adapter.removeListener()//чтобы не возникало утечек памяти
        _binding = null//чтобы не возникало утечек памяти
        super.onDestroy()
    }


    private fun getLocation() {
        binding.root.showSnackBarShort("Идет запрос текущего местоположения...")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //Затем мы получаем наиболее подходящий провайдер
            //геолокации по критериям.
            //Система сама определит, какой провайдер сейчас доступен, и через него будет передавать
            //координаты. Если будет доступен GPS, то координаты будут точнее. Если будут доступны координаты
            //только по сотовым вышкам, позиционирование будет приблизительным.
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria().apply { accuracy = Criteria.ACCURACY_COARSE }
            val provider = locationManager?.getBestProvider(criteria, true)
            if (provider == null) binding.root.showSnackBarShort("Включите службы местоположения!")
            provider?.let {
                // подключаеся к 2 провайдерам, ждем - кто быстрее отработает
//                locationManager?.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER,
//                    REFRESH_PERIOD_NETWORK,
//                    MINIMAL_DISTANCE,
//                    locationListener
//                )
                locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    REFRESH_PERIOD_GPS,
                    MINIMAL_DISTANCE,
                    locationListener
                )
            }
        }
    }

    private val locationListener = object : LocationListener {
        //вызывается, когда приходят новые данные о местоположении.
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddress(it, location)
                Log.d(TAG, "${location.latitude} - ${location.longitude}")
            }
        }

        //вызывается при изменении статуса: Available или Unavailable. На Android Q и выше он всегда будет возвращать Available.
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        //вызывается, если пользователь включил GPS.
        override fun onProviderEnabled(provider: String) {
            Log.d(TAG, "пользователь включил GPS")
        }

        //вызывается, если пользователь выключил GPS или сразу, если GPS был отключён изначально.
        override fun onProviderDisabled(provider: String) {
            Log.d(TAG, "пользователь выключил GPS!")
        }
    }

    private fun getAddress(
        context: Context,
        location: Location
    ) {
        val geoCoder = Geocoder(context, Locale("ru_RU"))
        Thread {
            try {
                val time = measureTimeMillis {
                    //Передаём широту, долготу и желаемое количество адресов по заданным координатам
                    val address =
                        geoCoder.getFromLocation(location.latitude, location.longitude, 100)
                    locationManager?.removeUpdates(locationListener)
                    (activity as BaseActivity).showWeatherDetails(
                        Weather(
                            City(
                                address[0].locality,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                Log.d(TAG, "$time")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

}