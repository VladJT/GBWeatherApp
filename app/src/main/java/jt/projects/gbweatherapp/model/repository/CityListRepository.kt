package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.Weather

interface CityListRepository {
    fun getCityList(choose: Location, callback: CityListLoadCallback): List<Weather>
}

interface RepositoryWeatherAll {
    fun getWeatherAll(callback: AllWeatherLoadCallback)
}