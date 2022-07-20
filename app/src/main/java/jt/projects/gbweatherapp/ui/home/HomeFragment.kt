package jt.projects.gbweatherapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

class HomeFragment : Fragment() {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //    menu.findItem(R.id.action_clean_room).isVisible = false
    }

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
    }


    override fun onDestroy() {
        adapter.removeListener()//чтобы не возникало утечек памяти
        _binding = null//чтобы не возникало утечек памяти
        super.onDestroy()
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

}