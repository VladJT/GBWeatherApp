package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import jt.projects.gbweatherapp.utils.WeatherCondition
import java.io.IOException

class CityListRepositoryImpl : CityListRepository {

    lateinit var vmCallback : MyLargeSuperCallback

    override fun getCityList(choose: Int, callback: MyLargeSuperCallback): List<Weather> {
        vmCallback = callback
        return when (choose) {
            1 -> getCityListFromInternet()
            2 -> getCityListFromLocalStorageRus()
            3 -> getCityListFromLocalStorageWorld()
            else -> {
                getCityListFromLocalStorageRus()
            }
        }
    }

    private fun getCityListFromInternet(): List<Weather> {
        TODO("ПЕРЕДЕЛАТЬ!")
        return updateDataWithInternet(getRussianCities())
    }

    private fun getCityListFromLocalStorageRus(): List<Weather> {
        return updateDataWithInternet(getRussianCities())
    }

    private fun getCityListFromLocalStorageWorld(): List<Weather> {
        return updateDataWithInternet(getWorldCities())
    }


    fun updateDataWithInternet(cities: List<Weather>): List<Weather> {
        val repository = RepositoryDetailsOkHttpImpl()
        for (city in cities) {
            val callback = object : MyLargeSuperCallback {
                override fun onResponse(weatherDTO: WeatherDTO) {
                    city.temperature = weatherDTO.fact.temp
                    city.feelsLike = weatherDTO.fact.feelsLike
                    city.condition = WeatherCondition.getRusName(weatherDTO.fact.condition)
                    city.icon = weatherDTO.fact.icon
                    vmCallback.onResponse(weatherDTO)
                }
                override fun onFailure(e: IOException) {
                    throw IOException("load city list error")
                }
            }
            repository.getWeather(city.city.lat, city.city.lon, callback)
        }
        return cities
    }
}