package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather


fun interface RepositoryWeather {
    fun getWeather(city: City, callback: WeatherLoadCallback)
}

interface RepositoryAllWeather {
    fun getWeatherAll(callback: WeatherListLoadCallback)
}

interface WeatherEditable {
    fun addWeather(weather: Weather)
    fun deleteAll()
}

interface RepositoryHistoryWeather : WeatherEditable {
    fun getHistoryByLocation(city: City, callback: WeatherListLoadCallback)
}

interface RepositoryCityList {
    fun getCityList(choose: Location, callback: CommonCallback): List<Weather>
}
