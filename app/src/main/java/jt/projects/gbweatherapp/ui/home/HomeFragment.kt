package jt.projects.gbweatherapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import jt.projects.gbweatherapp.databinding.FragmentHomeBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.ui.favorites.FavoritesFragment
import jt.projects.gbweatherapp.utils.WeatherCondition
import jt.projects.gbweatherapp.viewmodel.AppState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    companion object{
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.textInfo.text = """Здесь будет отображаться детальная погода по стартовому городу
            Город будет задаваться как стартовый из фрагмента поиска (будет хранится в shared_pref)
            В нижней половине экрана в RecyclerView будет погода по часам (или какая-то иная списочная информация)"""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val observer = Observer<AppState> { renderData(it) }// it = конкрeтный экзмепляр AppState
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.loadWeather()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding.loadingLayout.visibility = View.GONE
                updateUi(weatherData)
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

    private fun updateUi(weather: Weather) {
        with(binding.includeCardWeather) {
            with(weather) {
                cityName.text = city.name
                cityCoordinates.text = String.format(
                    "lt/ln: %s, %s",
                    weather.city.lat.toString(),
                    weather.city.lon.toString()
                )
                temperatureValue.text = weatherData.temperature.toString() + "\u2103"
                temperatureValueBig.text = weatherData.temperature.toString() + "\u2103"
                feelsLikeValue.text = weatherData.feelsLike.toString() + "\u2103"
                humidityValue.text = weatherData.feelsLike.toString() + "%"
                pressureValue.text = weatherData.pressure_mm.toString()
                windSpeedValue.text = weatherData.wind_speed.toString()
                conditionValue.text = WeatherCondition.getRusName(weatherData.condition)
            }
        }
    }
}