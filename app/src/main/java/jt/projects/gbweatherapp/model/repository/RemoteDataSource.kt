package jt.projects.gbweatherapp.model.repository

import com.google.gson.GsonBuilder
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//это класс, где происходит запрос на сервер. Это наш источник данных. Здесь мы
//создаём инстанс OkHttp, формируем запрос и отправляем его. Результаты запроса станут
//обрабатываться во ViewModel — там будет находиться наш callback
class RemoteDataSource {

    fun getWeatherDetailsWithOkHttp(requestLink: String, callback: okhttp3.Callback) {
        val builder: Request.Builder = Request.Builder().apply {
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }

    fun getWeatherDetailsWithRetrofit(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        val weatherApi = Retrofit.Builder().baseUrl("https://api.weather.yandex.ru/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(WeatherAPI::class.java)
        weatherApi.getWeather(
            BuildConfig.WEATHER_API_KEY, lat,
            lon
        ).enqueue(callback)

    }

}