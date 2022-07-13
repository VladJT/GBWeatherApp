package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.Weather

interface CityListRepository {
    fun getWeatherFromInternet(): List<Weather>
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}