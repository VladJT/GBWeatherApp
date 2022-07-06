package jt.projects.gbweatherapp.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.utils.OnItemViewClickListener
import jt.projects.gbweatherapp.utils.showSnackBarShort
import jt.projects.gbweatherapp.utils.showSnackBarWithAction
import jt.projects.gbweatherapp.viewmodel.AppState

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private var isDataSetRus: Boolean = true

    companion object {
        const val MY_DEFAULT_DURATION: Long = 300
        fun newInstance() = HomeFragment()
    }


    private val adapter = HomeFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            activity?.supportFragmentManager?.also { manager ->
                val bundle = Bundle()
                bundle.putParcelable(WeatherDetailsFragment.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .add(R.id.fragment_container, WeatherDetailsFragment.newInstance(bundle))
                    .addToBackStack("").commit()
            }
        }
    })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val observer = Observer<AppState> { renderData(it) }// it = конкрeтный экзмепляр AppState

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java].also {
            it.getData().observe(viewLifecycleOwner, observer)
            it.getDataFromLocalSource(isDataSetRus)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        adapter.removeListener()//чтобы не возникало утечек памяти
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
            addDuration = MY_DEFAULT_DURATION
            changeDuration = MY_DEFAULT_DURATION
            removeDuration = MY_DEFAULT_DURATION
            moveDuration = MY_DEFAULT_DURATION
        }
        binding.mainFragmentRecyclerView.itemAnimator = animator
    }


    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus
        viewModel.getDataFromLocalSource(isDataSetRus)
    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(listOf(appState.weather))
                binding.root.showSnackBarShort("Данные по 1 городу успешно загружены")
            }
            is AppState.SuccessMulti -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weather)
                binding.root.showSnackBarShort(R.string.load_completed)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(listOf())
                val action = fun() { viewModel.getDataFromLocalSource(isDataSetRus) }
                binding.root.showSnackBarWithAction(
                    appState.error.message ?: "",
                    R.string.reload,
                    action
                )
            }
        }
    }

}