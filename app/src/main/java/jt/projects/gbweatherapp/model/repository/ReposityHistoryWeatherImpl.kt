package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.room.WeatherHistoryEntity
import jt.projects.gbweatherapp.utils.convertWeatherToHistory

class ReposityHistoryWeatherImpl : RepositoryHistoryWeather, WeatherEditable {

    override fun addWeather(weather: Weather) {
        Thread {
            MyApp.getWeatherDbAsyncMode().weatherDao()
                .insertHistory(convertWeatherToHistory(weather))
        }.start()
    }


    override fun getHistoryByLocation(city: City, callback: WeatherListLoadCallback) {
        Thread {
            val responce = convertToWeather(
                MyApp.getWeatherDbAsyncMode().weatherDao().getHistoryByLocation(city.lat, city.lon)
            )
            callback.onResponse(responce)
        }.start()
    }

    private fun convertToWeather(entityList: List<WeatherHistoryEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City("", it.lat, it.lon),
                temperature = it.temperature,
                now = it.now
            )
        }
    }
}