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
import com.google.android.material.snackbar.Snackbar
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentHomeBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.utils.OnItemViewClickListener
import jt.projects.gbweatherapp.viewmodel.AppState

class HomeFragment : Fragment() {
    private val MY_DEFAULT_DURATION: Long = 300

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    private val adapter = HomeFragmentAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if (manager != null) {
                val bundle = Bundle()
                bundle.putParcelable(WeatherDetailsFragment.BUNDLE_EXTRA, weather)
                manager.beginTransaction()
                    .add(R.id.fragment_container, WeatherDetailsFragment.newInstance(bundle))
                    .addToBackStack("").commit()

            }
        }
    })
    private var isDataSetRus: Boolean = true

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = activity as? AppCompatActivity
        actionBar?.supportActionBar?.subtitle = "Список городов"
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val observer = Observer<AppState> { renderData(it) }// it = конкрeтный экзмепляр AppState
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getDataFromLocalSource(isDataSetRus)
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
        // Добавим разделитель карточек
        val itemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.separator, null))
        binding.mainFragmentRecyclerView.addItemDecoration(itemDecoration)

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        val animator = DefaultItemAnimator()
        animator.addDuration = MY_DEFAULT_DURATION
        animator.changeDuration = MY_DEFAULT_DURATION
        animator.removeDuration = MY_DEFAULT_DURATION
        animator.moveDuration = MY_DEFAULT_DURATION
        binding.mainFragmentRecyclerView.itemAnimator = animator
    }


    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            binding.mainFragmentFAB.setImageResource(R.drawable.world)
        } else {
            binding.mainFragmentFAB.setImageResource(R.drawable.russia)
        }
        isDataSetRus = !isDataSetRus
        viewModel.getDataFromLocalSource(isDataSetRus)
    }


    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(listOf(appState.weatherData))
                Snackbar.make(
                    binding.root,
                    "Данные по 1 городу успешно загружены",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
            is AppState.SuccessMulti -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherData)
                Snackbar.make(binding.root, "Данные успешно загружены", Snackbar.LENGTH_SHORT)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                adapter.setWeather(listOf())
                Snackbar
                    .make(binding.root, appState.error.message!!, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload?") { viewModel.getDataFromLocalSource(isDataSetRus) }
                    .show()
            }
        }
    }

}