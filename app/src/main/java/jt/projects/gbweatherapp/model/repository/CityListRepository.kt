package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.Weather

interface CityListRepository {
    fun getCityListFromInternet(): List<Weather>
    fun getCityListFromLocalStorageRus(): List<Weather>
    fun getCityListFromLocalStorageWorld(): List<Weather>
}