package jt.projects.gbweatherapp.ui.weatherdetails

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import jt.projects.gbweatherapp.databinding.WeatherDetailsFragmentBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.WeatherCondition
import jt.projects.gbweatherapp.utils.getLines
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

private const val YANDEX_API_KEY = "87f4f61a-7365-417f-9c86-97c408a842d7"


class WeatherDetailsFragment : Fragment() {

    private var _binding: WeatherDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    companion object {
        const val BUNDLE_EXTRA = "weather"

        fun newInstance(bundle: Bundle): WeatherDetailsFragment =
            WeatherDetailsFragment().apply { arguments = bundle }
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
        loadWeather()
    }

    private fun renderData(weather: Weather) {
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
                feelsLikeValue.text = "${fact.feels_like}\u2103"
                humidityValue.text = "${fact.humidity}%"
                pressureValue.text = fact.pressure_mm.toString()
                windSpeedValue.text = fact.wind_speed.toString()
                conditionValue.text = WeatherCondition.getRusName(fact.condition)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun loadWeather() {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
            val handler = Handler()
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        YANDEX_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))// преобразование ответа от сервера (JSON) в модель данных
                    val weatherDTO = Gson().fromJson(getLines(bufferedReader), Weather::class.java)
                    handler.post { renderData(weatherDTO) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
        }
    }

}