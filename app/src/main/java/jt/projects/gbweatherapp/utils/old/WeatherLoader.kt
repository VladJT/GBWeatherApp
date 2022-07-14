package jt.projects.gbweatherapp.utils.old

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import jt.projects.gbweatherapp.utils.REQUEST_GET
import jt.projects.gbweatherapp.utils.REQUEST_TIMEOUT
import jt.projects.gbweatherapp.utils.getLines
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class WeatherLoader(
    private val weatherLoaderListener: WeatherLoaderListener,
    private val lat: Double,
    private val lon: Double
) {

    private val tag = "WeatherLoader"

    interface WeatherLoaderListener {
        fun onLoaded(weatherDTO: WeatherDTO)
        fun onFailed(throwable: Throwable)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() {
        try {
            val uri =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")
            Thread {
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
                    Handler(Looper.getMainLooper()).post { weatherLoaderListener.onLoaded(weatherDTO) }

                } catch (e: Exception) {
                    Log.e(tag, "Fail connection", e)
                    weatherLoaderListener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e(tag, "Fail URI", e)
            weatherLoaderListener.onFailed(e)
        }
    }
}