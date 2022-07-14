package jt.projects.gbweatherapp.model.repository

import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import jt.projects.gbweatherapp.utils.REQUEST_URL
import okhttp3.*
import java.io.IOException

class RepositoryDetailsOkHttpImpl : RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double, callback: WeatherDTOLoadCallback) {
        val client = OkHttpClient()
        val builder = Request.Builder().apply {
            addHeader(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
            url(String.format(REQUEST_URL, lat, lon))
        }
        val request: Request = builder.build()
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                //if (response.code in 200..299) { }
                if (response.isSuccessful && response.body != null) {
                    response.body?.let {
                        val responseString = it.string()
                        val weatherDTO =
                            Gson().fromJson((responseString), WeatherDTO::class.java)
                        callback.onResponse(weatherDTO)
                    }
                } else {

                    callback.onFailure(IOException("Ok Http Exception - 403/404"))
                }
            }
        })
    }
}