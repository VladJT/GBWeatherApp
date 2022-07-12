package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.dto.WeatherDTO

//интерфейс будет обозначать работу с данными на экране DetailsFragment.
//метод принимает в качестве аргументов строку для запроса на сервер и callback.
interface DetailsRepository {
    fun getWeatherDetailsFromServerOkHttp(requestLink: String, callback: okhttp3.Callback)

    fun getWeatherDetailsFromServerRetrofit(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}
