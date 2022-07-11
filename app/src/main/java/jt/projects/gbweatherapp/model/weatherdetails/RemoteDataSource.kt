package jt.projects.gbweatherapp.model.weatherdetails

import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

//это класс, где происходит запрос на сервер. Это наш источник данных. Здесь мы
//создаём инстанс OkHttp, формируем запрос и отправляем его. Результаты запроса станут
//обрабатываться во ViewModel — там будет находиться наш callback
class RemoteDataSource {

    fun getWeatherDetails(requestLink: String, callback: Callback) {
        val builder: Request.Builder = Request.Builder().apply {
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY)
            url(requestLink)
        }
        OkHttpClient().newCall(builder.build()).enqueue(callback)
    }

}