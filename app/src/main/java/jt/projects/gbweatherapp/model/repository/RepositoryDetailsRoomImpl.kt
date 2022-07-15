package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.room.WeatherEntity

class RepositoryDetailsRoomImpl : RepositoryDetails, RepositoryAppendable, RepositoryWeatherAll {
    override fun getWeather(city: City, callback: WeatherLoadCallback) {
        callback.onResponse(
            convertToWeather(
                MyApp.getWeatherDatabase().weatherDao().getWeatherByLocation(city.lat, city.lon)
            ).last()
        )
    }

    fun convertToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City(it.name, it.lat, it.lon),
                it.temperature,
                it.feelsLike,
                it.condition,
                it.icon
            )
        }
    }


    override fun addWeather(weather: Weather) {
        MyApp.getWeatherDatabase().weatherDao().insert(convertWeatherToEntity(weather))
    }

    private fun convertEntityToWeather(eList: List<WeatherEntity>): List<Weather> {
        return eList.map {
            Weather(
                City(it.name, it.lat, it.lon), it.temperature, it.feelsLike, it.condition, it.icon
            )
        }
    }

    private fun convertWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            0, weather.city.name, weather.city.lat, weather.city.lon,
            weather.temperature, weather.feelsLike, weather.condition, weather.icon
        )
    }

    override fun getWeatherAll(callback: AllWeatherLoadCallback) {
        callback.onResponse(
            convertEntityToWeather(
                MyApp.getWeatherDatabase().weatherDao().getWeatherAll()
            )
        )
    }


}