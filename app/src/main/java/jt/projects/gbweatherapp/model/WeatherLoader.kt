package jt.projects.gbweatherapp.model

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
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
                            requestMethod = "GET"
                            addRequestProperty(
                                "X-Yandex-API-Key",
                                BuildConfig.WEATHER_API_KEY
                                //ключ можно будет получать из кода: в автосгенерированном
                                //средой разработки классе BuildConfig появится поле WEATHER_API_KEY
                            )
                            readTimeout = 10000
                        }
                    // преобразование ответа от сервера (JSON) в модель данных
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))

                    val weatherDTO =
                        Gson().fromJson(getLines(bufferedReader), WeatherDTO::class.java)
                    Handler(Looper.getMainLooper()).post { weatherLoaderListener.onLoaded(weatherDTO) }

                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    weatherLoaderListener.onFailed(e)
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            weatherLoaderListener.onFailed(e)
        }
    }
}