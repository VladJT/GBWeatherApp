package jt.projects.gbweatherapp.model.repository

import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import jt.projects.gbweatherapp.utils.REQUEST_URL
import jt.projects.gbweatherapp.utils.convertDTOtoModel
import okhttp3.*
import java.io.IOException

class RepositoryDetailsOkHttpImpl : RepositoryDetails {
    override fun getWeather(city: City, callback: WeatherLoadCallback) {
        val client = OkHttpClient()
        val builder = Request.Builder().apply {
            addHeader(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
            url(String.format(REQUEST_URL, city.lat, city.lon))
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
                        callback.onResponse(convertDTOtoModel(weatherDTO, city))
                    }
                } else {

                    callback.onFailure(IOException("Ok Http Exception - 403/404"))
                }
            }
        })
    }
}