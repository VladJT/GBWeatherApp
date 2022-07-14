package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherAPI {
    @GET("v2/forecast")//указывается endpoint ссылки (v2/forecast)

    fun getWeather(
        @Header(REQUEST_API_KEY) token: String,//заголовок (@Header),
        @Query("lat") lat: Double,//два параметра запроса (@Query)
        @Query("lon") lon: Double//
    ): Call<WeatherDTO>//Возвращает метод уже готовый класс с ответом от сервера (WeatherDTO)
}
