package jt.projects.gbweatherapp.ui.weatherdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import jt.projects.gbweatherapp.databinding.WeatherDetailsFragmentBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.WeatherCondition


class WeatherDetailsFragment : Fragment() {

    private var _binding: WeatherDetailsFragmentBinding? = null
    private val binding get() = _binding!!

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
        arguments?.let { args ->
            args.getParcelable<Weather>(BUNDLE_EXTRA)?.let {
                renderData(it)
            }
        }
        binding.buttonBack.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
    }

    private fun renderData(weather: Weather) {
        with(binding.includeWeatherCard) {
            weather.run {
                cityName.text = city.name
                cityCoordinates.text = String.format(
                    "lt/ln: %s, %s",
                    city.lat.toString(),
                    city.lon.toString()
                )
                temperatureValue.text = "${weatherData.temperature}\u2103"
                temperatureValueBig.text = "${weatherData.temperature}\u2103"
                feelsLikeValue.text = "${weatherData.feelsLike}\u2103"
                humidityValue.text = "${weatherData.humidity}%"
                pressureValue.text = weatherData.pressure_mm.toString()
                windSpeedValue.text = weatherData.wind_speed.toString()
                conditionValue.text = WeatherCondition.getRusName(weatherData.condition)
            }
        }
    }

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): WeatherDetailsFragment =
            WeatherDetailsFragment().apply { arguments = bundle }
    }
}