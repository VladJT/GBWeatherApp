package jt.projects.gbweatherapp.model.weatherdetails

import okhttp3.Callback

//интерфейс будет обозначать работу с данными на экране DetailsFragment.
//метод принимает в качестве аргументов строку для запроса на сервер и callback для OkHttp.
interface DetailsRepository {
    fun getWeatherDetailsFromServer(requestLink: String, callback: Callback)
}
