package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather


fun interface RepositoryDetails {
    fun getWeather(city: City, callback: WeatherLoadCallback)
}

fun interface RepositoryAppendable {
    fun addWeather(weather: Weather)
}