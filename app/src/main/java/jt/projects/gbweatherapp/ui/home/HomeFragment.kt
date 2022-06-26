package jt.projects.gbweatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentHomeBinding
import jt.projects.gbweatherapp.viewmodel.AppState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    private val adapter = HomeFragmentAdapter()
    private var isDataSetRus: Boolean = true

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = adapter
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
                //updateUi(weatherData)
                adapter.setWeather(appState.weatherData)
                Snackbar.make(binding.root, "Данные успешно загружены", Snackbar.LENGTH_SHORT)
                    .show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.root, appState.error.message!!, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload?") { viewModel.loadWeather() }
                    .show()
            }
        }
    }

//    private fun updateUi(weather: Weather) {
//        with(binding.includeCardWeather) {
//            with(weather) {
//                cityName.text = city.name
//                cityCoordinates.text = String.format(
//                    "lt/ln: %s, %s",
//                    weather.city.lat.toString(),
//                    weather.city.lon.toString()
//                )
//                temperatureValue.text = weatherData.temperature.toString() + "\u2103"
//                temperatureValueBig.text = weatherData.temperature.toString() + "\u2103"
//                feelsLikeValue.text = weatherData.feelsLike.toString() + "\u2103"
//                humidityValue.text = weatherData.feelsLike.toString() + "%"
//                pressureValue.text = weatherData.pressure_mm.toString()
//                windSpeedValue.text = weatherData.wind_speed.toString()
//                conditionValue.text = WeatherCondition.getRusName(weatherData.condition)
//            }
//        }
//    }
}