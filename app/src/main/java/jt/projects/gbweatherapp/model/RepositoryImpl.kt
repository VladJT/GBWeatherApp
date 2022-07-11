package jt.projects.gbweatherapp.model

import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import okhttp3.*
import java.io.IOException

class RepositoryImpl : Repository {
    override fun getWeatherFromInternet(): Weather {
        return getRussianCities()[0]
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        val cities = getRussianCities()
        return loadFromOkHttp(cities)
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        val cities = getWorldCities()
        return loadFromOkHttp(cities)
    }


    fun loadFromOkHttp(cities: List<Weather>): List<Weather> {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder().apply {  // Создаём строителя запроса
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY) // Создаём заголовок запроса
        }

        for (i in cities.indices) {
            builder.url("https://api.weather.yandex.ru/v2/forecast?lat=${cities[i].city.lat}&lon=${cities[i].city.lon}")
            val request: Request = builder.build() // Создаём запрос
            val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем
            call.enqueue(object : Callback {
                val handler: Handler = Handler(Looper.getMainLooper())

                // Вызывается, если ответ от сервера пришёл
                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response) {
                    val serverResponce: String? = response.body()?.string()
                    // Синхронизируем поток с потоком UI
                    if (response.isSuccessful && serverResponce != null) {
                        handler.post {
                            val weatherDTO =
                                Gson().fromJson(serverResponce, WeatherDTO::class.java)
                            with(cities[i]) {
                                temperature = weatherDTO.fact.temp
                                feelsLike = weatherDTO.fact.feelsLike
                                condition =
                                    jt.projects.gbweatherapp.utils.WeatherCondition.getRusName(
                                        weatherDTO.fact.condition
                                    )
                            }

                        }
                    } else {
                        //     TODO(PROCESS_ERROR)
                    }
                }

                // Вызывается при сбое в процессе запроса на сервер
                override fun onFailure(call: Call?, e: IOException?) {
                    //    TODO(PROCESS_ERROR)
                }
            })
        }
        return cities
    }
}