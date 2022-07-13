package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.dto.WeatherDTO
import java.io.IOException



fun interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double, callback: MyLargeSuperCallback)
}

interface MyLargeSuperCallback {
    fun onResponse(weatherDTO: WeatherDTO)
    fun onFailure(e: IOException)
}