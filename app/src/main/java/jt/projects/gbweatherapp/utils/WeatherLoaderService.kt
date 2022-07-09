package jt.projects.gbweatherapp.utils

import android.app.IntentService
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.ui.weatherdetails.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val BUNDLE_CITY_KEY = "BUNDLE_CITY_KEY"
private const val TAG = "WeatherLoaderService"

class WeatherLoaderService(name: String = "WeatherLoaderService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val city = intent.getParcelableExtra<City>(BUNDLE_CITY_KEY)
            val lat = city?.lat ?: 0
            val lon = city?.lon ?: 0
            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
            } else {
                loadWeather(lat.toString(), lon.toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(lat: String, lon: String) {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = (uri.openConnection() as HttpsURLConnection)
                    .apply {
                        requestMethod = REQUEST_GET
                        addRequestProperty(
                            REQUEST_API_KEY,
                            BuildConfig.WEATHER_API_KEY
                        )
                        readTimeout = REQUEST_TIMEOUT
                    }
                // преобразование ответа от сервера (JSON) в модель данных
                val bufferedReader =
                    BufferedReader(InputStreamReader(urlConnection.inputStream))

                val weatherDTO =
                    Gson().fromJson(getLines(bufferedReader), WeatherDTO::class.java)
                onResponse(weatherDTO)

            } catch (e: Exception) {
                Log.e(TAG, "Fail connection", e)
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        } catch (e: MalformedURLException) {
            Log.e(TAG, "Fail URI", e)
            onMalformedURL()
        }
    }

    private fun onResponse(weatherDTO: WeatherDTO?) {
        if (weatherDTO?.fact == null) {
            onEmptyResponse()
        } else {
            onSuccessResponse(weatherDTO!!)
        }
    }

    private fun onSuccessResponse(weatherDTO: WeatherDTO) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_DTO_EXTRA, weatherDTO)
        sendIt()
    }

    private fun onEmptyResponse() {
        putLoadResult(DETAILS_RESPONSE_EMPTY_EXTRA)
        sendIt()
    }

    private fun onMalformedURL() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        sendIt()
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        sendIt()
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        sendIt()
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        sendIt()
    }

    private fun putLoadResult(result: String) =
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)

    private fun sendIt()  {
        Log.i(TAG, "request: ".plus(broadcastIntent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)))
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent)}

}