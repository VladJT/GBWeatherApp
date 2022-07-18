package jt.projects.gbweatherapp.ui.weatherdetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.databinding.FragmentWeatherDetailsBinding

import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.*
import jt.projects.gbweatherapp.utils.old.WeatherLoader
import jt.projects.gbweatherapp.viewmodel.AppState

const val BUNDLE_EXTRA = "weather"
const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_DTO_EXTRA = "DETAILS_DTO_EXTRA"

class WeatherDetailsFragment : Fragment() {

    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private lateinit var viewModel: WeatherDetailsViewModel
    private lateinit var viewModelHistory: HistoryDetailsViewModel

    companion object {
        fun newInstance(bundle: Bundle): WeatherDetailsFragment =
            WeatherDetailsFragment().apply { arguments = bundle }
    }

    @Deprecated("Используется для загрузки данных через BroadcastReceiver")
    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)
            when (result) {
                DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                    intent.getParcelableExtra<WeatherDTO>(DETAILS_DTO_EXTRA)?.also {
                        //   renderData(convertDTOtoModel(it, ))
                    }
                }
                else -> {
                    //   binding.progressBarDetails.visibility = View.GONE
                }
            }
            binding.root.showSnackBarShort(result)
        }
    }

    @Deprecated("Используется для загрузки данных через WeatherLoader")
    private val weatherLoadListener = object : WeatherLoader.WeatherLoaderListener {
        override fun onLoaded(weatherDTO: WeatherDTO) {
            //    renderData(convertDTOtoModel(weatherDTO))
        }

        override fun onFailed(throwable: Throwable) {
            binding.root.showSnackBarShort(throwable.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        context?.let {
//            LocalBroadcastManager.getInstance(it)
//                .registerReceiver(
//                    loadResultsReceiver,
//                    IntentFilter(DETAILS_INTENT_FILTER)
//                )
//        }
    }

    override fun onDestroy() {
        _binding = null//чтобы не возникало утечек памяти
        // останавливаем BroadcastReciever
//        context?.let {
//            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
//        }
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
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA)!!
        binding.buttonBack.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }

        viewModel = ViewModelProvider(this)[WeatherDetailsViewModel::class.java].also { it ->
            it.getDetailsLiveData().observe(viewLifecycleOwner, Observer { renderDataVM(it) })
            it.getWeather(weatherBundle.city)
            //    it.getWeatherFromRemoteSource("https://api.weather.yandex.ru/v2/forecast?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
        }

        viewModelHistory = ViewModelProvider(this)[HistoryDetailsViewModel::class.java].also { it ->
            it.getDetailsLiveData().observe(viewLifecycleOwner, Observer { renderDataHistory(it) })
            //    it.getWeatherFromRemoteSource("https://api.weather.yandex.ru/v2/forecast?lat=${weatherBundle.city.lat}&lon=${weatherBundle.city.lon}")
        }

        binding.buttonShowHistory.setOnClickListener() {
            viewModelHistory.getHistoryByLocation(
                weatherBundle.city
            )
        }

        binding.buttonAddToHistory.setOnClickListener() {
            viewModelHistory.addHistory(weatherBundle)
        }
        // загружаем данные через сервис
//        context?.let {
//            it.startService(Intent(it, WeatherLoaderService::class.java).apply {
//                putExtra(BUNDLE_CITY_KEY, weatherBundle.city)
//            })
//        }
    }

    private fun renderDataHistory(appState: AppState<List<Weather>>) {
        when (appState) {
            is AppState.Success<*> -> {
                showLoadLayout(false)
                showHistory(appState.data as List<Weather>)
            }
            is AppState.Loading -> {
                showLoadLayout(true)
            }
            is AppState.Error -> {
                showLoadLayout(false)
                binding.root.showSnackBarShort("Ошибка загрузки детализации истории")
            }
        }
    }

    private fun showHistory(weatherList: List<Weather>) {
        val sb = StringBuilder()
        for (weather in weatherList) {
            sb.append(weather.now.toDateTime())
                .append(" ")
                .append(weather.temperature.toString().toTemperature())
                .append("\n")
        }
        //(activity as? BaseActivity)?.showMsgDialog("История изменения погоды: ", sb.toString())
        val customDialogView = layoutInflater.inflate(R.layout.dialog_history, null)
        val c = requireContext()
        val dialog = AlertDialog.Builder(c)
            .setTitle("История изменения погоды")
            .setView(customDialogView)
            .setIcon(android.R.drawable.ic_menu_recent_history)
            .setNeutralButton(c.getText(android.R.string.no), null)
            .setPositiveButton(c.getText(android.R.string.yes)) { dialog, which -> }
        val textViewHistory = customDialogView.findViewById<TextView>(R.id.textViewHistory)
        textViewHistory.text = sb.toString()
        dialog.show()
    }

    private fun renderDataVM(appState: AppState<Weather>) {
        when (appState) {
            is AppState.Success<*> -> {
                showLoadLayout(false)
                renderData(appState.data as Weather)
            }
            is AppState.Loading -> {
                showLoadLayout(true)
            }
            is AppState.Error -> {
                showLoadLayout(false)
                binding.root.showSnackBarShort("Ошибка загрузки детализации погоды")
            }
        }
    }

    private fun showLoadLayout(isLoading: Boolean) {
        if (isLoading) {
            binding.includeWeatherCard.root.visibility = View.GONE
            binding.loadingLayout.visibility = View.VISIBLE
        } else {
            binding.includeWeatherCard.root.visibility = View.VISIBLE
            binding.loadingLayout.visibility = View.GONE
        }
    }

    private fun renderData(weather: Weather) {
        with(binding.includeWeatherCard) {
            cityIcon.load("https://img4.goodfon.com/original/1680x1050/7/28/skyscraper-sunset-new-york.jpg")
            weatherIcon.load(String.format(ICON_URL, weather.icon))

            cityName.text = weatherBundle.city.name
            cityCoordinates.text = String.format(
                "lt/ln: %s, %s",
                weatherBundle.city.lat.toString(),
                weatherBundle.city.lon.toString()
            )

            temperatureValue.text = weather.temperature.toString().toTemperature()
            temperatureValueBig.text = weather.temperature.toString().toTemperature()
            feelsLikeValue.text = weather.feelsLike.toString().toTemperature()
            humidityValue.text = "${weather.humidity}%"
            pressureValue.text = weather.pressureMm.toString()
            windSpeedValue.text = weather.windSpeed.toString()
            conditionValue.text = WeatherCondition.getRusName(weather.condition)
            textViewDateOfWeather.text = weather.now.toDateTime()
            precTypeValue.text = getPrecType(weather.precType)

        }
    }
}