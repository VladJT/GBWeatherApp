package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather


fun interface RepositoryWeather {
    fun getWeather(city: City, callback: WeatherLoadCallback)
}

interface RepositoryAllWeather {
    fun getWeatherAll(callback: WeatherListLoadCallback)
}

fun interface WeatherAppendable {
    fun addWeather(weather: Weather)
}

interface RepositoryCityList {
    fun getCityList(choose: Location, callback: CommonCallback): List<Weather>
}
