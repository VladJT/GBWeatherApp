package jt.projects.gbweatherapp.ui.weatherdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import jt.projects.gbweatherapp.databinding.WeatherDetailsFragmentBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.WeatherLoader
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.WeatherCondition
import jt.projects.gbweatherapp.utils.showSnackBarShort

class WeatherDetailsFragment : Fragment() {

    private var _binding: WeatherDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): WeatherDetailsFragment =
            WeatherDetailsFragment().apply { arguments = bundle }
    }

    private val weatherLoadListener = object : WeatherLoader.WeatherLoaderListener {
        override fun onLoaded(weatherDTO: WeatherDTO) {
            renderData(weatherDTO)
        }

        override fun onFailed(throwable: Throwable) {
            binding.root.showSnackBarShort(throwable.message)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)// дает фрагменту доступ к меню Активити
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.subtitle = "Сведения о городе"
        }
        _binding = WeatherDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable<Weather>(BUNDLE_EXTRA)!!
        binding.buttonBack.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }

        val loader =
            WeatherLoader(weatherLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding.includeWeatherCard) {
            cityName.text = weatherBundle.city.name
            cityCoordinates.text = String.format(
                "lt/ln: %s, %s",
                weatherBundle.city.lat.toString(),
                weatherBundle.city.lon.toString()
            )
            weather?.run {
                temperatureValue.text = "${fact.temp}\u2103"
                temperatureValueBig.text = "${fact.temp}\u2103"
                feelsLikeValue.text = "${fact.feelsLike}\u2103"
                humidityValue.text = "${fact.humidity}%"
                pressureValue.text = fact.pressureMm.toString()
                windSpeedValue.text = fact.windSpeed.toString()
                conditionValue.text = WeatherCondition.getRusName(fact.condition)
            }
        }
    }
}