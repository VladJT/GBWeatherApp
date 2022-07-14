package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.dto.WeatherDTO
import java.io.IOException

interface WeatherDTOLoadCallback {
    fun onResponse(weatherDTO: WeatherDTO)
    fun onFailure(e: IOException)
}

interface CityListLoadCallback {
    fun onResponse()
    fun onFailure(e: IOException)
}