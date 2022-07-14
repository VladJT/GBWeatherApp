package jt.projects.gbweatherapp.model.repository

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import jt.projects.gbweatherapp.utils.REQUEST_URL
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class CityListRepositoryImpl : CityListRepository {

    override fun getCityList(choose: Int): List<Weather> {
        return when (choose) {
            1 -> getCityListFromInternet()
            2 -> getCityListFromLocalStorageRus()
            3 -> getCityListFromLocalStorageWorld()
            else -> {
                getCityListFromLocalStorageRus()
            }
        }
    }

    private fun getCityListFromInternet(): List<Weather> {
        TODO("ПЕРЕДЕЛАТЬ!")
        return loadFromOkHttp(getRussianCities())
    }

    private fun getCityListFromLocalStorageRus(): List<Weather> {
        val cities = getRussianCities()
        return loadFromOkHttp(cities)
    }

    private fun getCityListFromLocalStorageWorld(): List<Weather> {
        val cities = getWorldCities()
        return loadFromOkHttp(cities)
    }


    fun loadFromOkHttp(cities: List<Weather>): List<Weather> {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder().apply {  // Создаём строителя запроса
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY) // Создаём заголовок запроса
        }

        for (i in cities.indices) {
            builder.url(String.format(REQUEST_URL, cities[i].city.lat, cities[i].city.lon))
            val request: Request = builder.build() // Создаём запрос
            val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем
            call.enqueue(object : okhttp3.Callback {
                val handler: Handler = Handler(Looper.getMainLooper())

                // Вызывается, если ответ от сервера пришёл
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    val serverResponce: String? = response.body?.string()
                    // Синхронизируем поток с потоком UI
                    if (response.isSuccessful && serverResponce != null) {
                        handler.post {
                            val weatherDTO =
                                Gson().fromJson(serverResponce, WeatherDTO::class.java)
                            with(cities[i]) {
                                temperature = weatherDTO.fact.temp
                                feelsLike = weatherDTO.fact.feelsLike
                                icon = weatherDTO.fact.icon
                                condition =
                                    jt.projects.gbweatherapp.utils.WeatherCondition.getRusName(
                                        weatherDTO.fact.condition
                                    )
                            }

                        }
                    } else {
                        throw IOException("403 404")
                    }
                }

                // Вызывается при сбое в процессе запроса на сервер
                override fun onFailure(call: Call, e: IOException) {
                    throw IOException("403 404")
                }
            })
        }
        return cities
    }
}