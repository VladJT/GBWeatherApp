package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.room.WeatherEntity
import jt.projects.gbweatherapp.utils.convertWeatherToEntity

class RepositoryRoomImpl : RepositoryWeather, RepositoryAllWeather, WeatherEditable {
    override fun getWeather(city: City, callback: WeatherLoadCallback) {
        Thread {
            val responce =
                MyApp.getWeatherDbAsyncMode().weatherDao().getWeatherByLocation(city.lat, city.lon)
            if (responce.isNotEmpty()) callback.onResponse(convertToWeather(responce).last())
            else callback.onResponse(null)
        }.start()
    }

    override fun getWeatherAll(callback: WeatherListLoadCallback) {
        Thread {
            val responce = convertToWeather(
                MyApp.getWeatherDbAsyncMode().weatherDao().getWeatherAll()
            )
            callback.onResponse(responce)
        }.start()
    }

    override fun addWeather(weather: Weather) {
        Thread {
            MyApp.getWeatherDbAsyncMode().weatherDao().insert(convertWeatherToEntity(weather))
        }.start()
    }

    override fun deleteAllWeatherByLocation(city: City) {
        Thread {
            MyApp.getWeatherDbAsyncMode().weatherDao().deleteByLocation(city.lat, city.lon)
        }.start()
    }

    override fun deleteAll() {
        Thread {
            MyApp.getWeatherDbAsyncMode().weatherDao().deleteAll()
        }.start()
    }

    private fun convertToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City(it.name, it.lat, it.lon),
                temperature = it.temperature,
                feelsLike = it.feelsLike,
                condition = it.condition,
                icon = it.icon,
                now = it.now
            )
        }
    }
}