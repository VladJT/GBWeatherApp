package jt.projects.gbweatherapp.model.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@RequiresApi(Build.VERSION_CODES.N)

class RepositoryStreamLoaderImpl : RepositoryWeather {
    override fun getWeather(city: City, callback: WeatherLoadCallback) {
        Thread {
            val uri = URL(String.format(REQUEST_URL, city.lat, city.lon))
            var myConnection: HttpsURLConnection? = null
            myConnection = uri.openConnection() as HttpsURLConnection
            try {
                myConnection = (uri.openConnection() as HttpsURLConnection)
                    .apply {
                        requestMethod = REQUEST_GET
                        addRequestProperty(
                            REQUEST_API_KEY,
                            BuildConfig.WEATHER_API_KEY
                        )
                        readTimeout = REQUEST_TIMEOUT
                    }

                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callback.onResponse(convertDTOtoModel(weatherDTO, city))
            } catch (e: IOException) {
                callback.onFailure(e)
            } finally {
                myConnection?.disconnect()
            }
        }.start()
    }

}