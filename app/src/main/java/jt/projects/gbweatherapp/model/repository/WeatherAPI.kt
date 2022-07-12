package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.dto.WeatherDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Этим интерфейсом мы описываем конкретный запрос на сервер — запрос на данные погоды с
//сервера Яндекса. Он формируется простым методом с аннотациями: указывается endpoint ссылки
//(v2/forecast), заголовок (@Header), а два параметра (@Query) запроса передаются в метод как
//аргументы. Возвращает метод уже готовый класс с ответом от сервера (WeatherDTO).
interface WeatherAPI {
    @GET("v2/forecast")
    fun getWeather(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}
