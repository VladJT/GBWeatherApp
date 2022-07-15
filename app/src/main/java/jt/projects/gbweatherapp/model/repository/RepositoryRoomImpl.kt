package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.room.WeatherEntity
import jt.projects.gbweatherapp.utils.convertWeatherToEntity

class RepositoryRoomImpl : RepositoryWeather, RepositoryAllWeather, WeatherAppendable {
    override fun getWeather(city: City, callback: WeatherLoadCallback) {
        callback.onResponse(
            convertToWeather(
                MyApp.getWeatherDatabase().weatherDao().getWeatherByLocation(city.lat, city.lon)
            ).last()
        )
    }

    override fun getWeatherAll(callback: WeatherListLoadCallback) {
        callback.onResponse(
            convertToWeather(
                MyApp.getWeatherDatabase().weatherDao().getWeatherAll()
            )
        )
    }

    override fun addWeather(weather: Weather) {
        MyApp.getWeatherDatabase().weatherDao().insert(convertWeatherToEntity(weather))
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