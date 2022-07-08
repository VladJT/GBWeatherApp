package jt.projects.gbweatherapp.ui.weatherdetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import jt.projects.gbweatherapp.databinding.WeatherDetailsFragmentBinding
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.*

const val BUNDLE_EXTRA = "weather"
const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_DTO_EXTRA = "DETAILS_DTO_EXTRA"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
private const val PROCESS_ERROR = "Обработка ошибки"

class WeatherDetailsFragment : Fragment() {

    private var _binding: WeatherDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather

    companion object {
        fun newInstance(bundle: Bundle): WeatherDetailsFragment =
            WeatherDetailsFragment().apply { arguments = bundle }
    }

    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> binding.root.showSnackBarShort(
                    DETAILS_INTENT_EMPTY_EXTRA
                )
                DETAILS_DATA_EMPTY_EXTRA -> binding.root.showSnackBarShort(DETAILS_DATA_EMPTY_EXTRA)
                DETAILS_RESPONSE_EMPTY_EXTRA -> binding.root.showSnackBarShort(
                    DETAILS_RESPONSE_EMPTY_EXTRA
                )
                DETAILS_REQUEST_ERROR_EXTRA -> binding.root.showSnackBarShort(
                    DETAILS_REQUEST_ERROR_EXTRA
                )
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> binding.root.showSnackBarShort(
                    DETAILS_REQUEST_ERROR_MESSAGE_EXTRA
                )
                DETAILS_URL_MALFORMED_EXTRA -> binding.root.showSnackBarShort(
                    DETAILS_URL_MALFORMED_EXTRA
                )
                DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                    binding.root.showSnackBarShort(DETAILS_RESPONSE_SUCCESS_EXTRA)
                    intent.getParcelableExtra<WeatherDTO>(DETAILS_DTO_EXTRA)?.also {
                        renderData(it)
                    }
                }
                else -> binding.root.showSnackBarShort(PROCESS_ERROR)
            }
        }
    }


    @Deprecated("Используется для загрузки данных через WeatherLoader")
    private val weatherLoadListener = object : WeatherLoader.WeatherLoaderListener {
        override fun onLoaded(weatherDTO: WeatherDTO) {
            renderData(weatherDTO)
        }

        override fun onFailed(throwable: Throwable) {
            binding.root.showSnackBarShort(throwable.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    loadResultsReceiver,
                    IntentFilter(DETAILS_INTENT_FILTER)
                )
        }
    }

    override fun onDestroy() {
        // останавливаем BroadcastReciever
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
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

        // старый способ
//        WeatherLoader(
//            weatherLoadListener,
//            weatherBundle.city.lat,
//            weatherBundle.city.lon
//        )?.loadWeather()

        binding.progressBarDetails.visibility = View.VISIBLE

        // загружаем данные через сервис
        context?.let {
            it.startService(Intent(it, WeatherLoaderService::class.java).apply {
                putExtra(
                    LATITUDE_EXTRA,
                    weatherBundle.city.lat
                )
                putExtra(
                    LONGITUDE_EXTRA,
                    weatherBundle.city.lon
                )
            })
        }
    }

    private fun renderData(weather: WeatherDTO) {
        binding.progressBarDetails.visibility = View.GONE
        with(binding.includeWeatherCard) {
            cityName.text = weatherBundle.city.name
            cityCoordinates.text = String.format(
                "lt/ln: %s, %s",
                weatherBundle.city.lat.toString(),
                weatherBundle.city.lon.toString()
            )
            weather.run {
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