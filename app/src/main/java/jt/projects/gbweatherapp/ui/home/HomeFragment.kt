package jt.projects.gbweatherapp.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jt.projects.gbweatherapp.BaseActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentHomeBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.OperationType
import jt.projects.gbweatherapp.ui.OnItemViewClickListener
import jt.projects.gbweatherapp.ui.weatherdetails.BUNDLE_EXTRA
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.utils.DURATION_ITEM_ANIMATOR
import jt.projects.gbweatherapp.utils.showSnackBarShort
import jt.projects.gbweatherapp.utils.showSnackBarWithAction
import jt.projects.gbweatherapp.viewmodel.AppState
import jt.projects.gbweatherapp.viewmodel.SharedPref
import java.io.IOException
import kotlin.system.measureTimeMillis

const val REQUEST_CODE_LOCATION = 45

private const val REFRESH_PERIOD =
    2000L//Запрашивать местоположение будем с периодичностью
private const val MINIMAL_DISTANCE = 0f//ли при перемещении телефона на 100 метров

class HomeFragment : Fragment() {
    private val TAG = "@@@"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val adapter = HomeFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.also { manager ->
                val bundle = Bundle()
                bundle.putParcelable(BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .add(R.id.fragment_container, WeatherDetailsFragment.newInstance(bundle))
                    .addToBackStack("").commit()
            }
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
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
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
                //    binding.root.showSnackBarShort(R.string.load_completed)
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
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    REFRESH_PERIOD,
                    MINIMAL_DISTANCE,
                    LocationListener)
            }
        }
    }

    private fun checkPermission(permission: String) {
        val permResult =
            ContextCompat.checkSelfPermission(requireContext(), permission)
        PackageManager.PERMISSION_GRANTED
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Доступ к локации")
                .setMessage("Объяснение Объяснение Объяснение Объяснение")
                .setPositiveButton("Предоставить доступ") { _, _ ->
                    permissionRequest(permission)
                }
                .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(permission)
        }
    }


    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == Manifest.permission.ACCESS_FINE_LOCATION
                    && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                    Log.d(TAG, "Доступ получен")
                }
            }
        }
    }

    private val LocationListener = object : LocationListener {
        //вызывается, когда приходят новые данные о местоположении.
        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddress(it, location)
                Log.d(TAG, "${location.latitude} - ${location.longitude}")
            }
        }
        //вызывается при изменении статуса: Available или Unavailable. На Android Q и выше он всегда будет возвращать Available.
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle){}
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
        val geoCoder = Geocoder(context)
        Thread {
            try {
                val time = measureTimeMillis {
                    geoCoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        100000)
                }
                Log.d(TAG, "$time")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

}