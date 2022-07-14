package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.Weather

interface CityListRepository {
    fun getCityList(choose: Int): List<Weather>
}