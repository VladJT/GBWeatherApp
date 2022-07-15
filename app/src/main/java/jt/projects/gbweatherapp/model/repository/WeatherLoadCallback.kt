package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.Weather
import java.io.IOException


interface WeatherLoadCallback {
    fun onResponse(weather: Weather)
    fun onFailure(e: IOException)
}

interface AllWeatherLoadCallback {
    fun onResponse(weather: List<Weather>)
    fun onFailure(e: IOException)
}

interface CityListLoadCallback {
    fun onResponse()
    fun onFailure(e: IOException)
}