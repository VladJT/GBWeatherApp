package jt.projects.gbweatherapp.model.repository

import android.util.Log
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import jt.projects.gbweatherapp.utils.WeatherCondition
import java.io.IOException

class CityListRepositoryImpl : CityListRepository {

    lateinit var vmCallback: MyLargeSuperCallback

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
        TODO("ПЕРЕДЕЛАТЬ (ТВОРЧЕСКИ)!")
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
        var totalCounter = 0
        lateinit var lastDTO: WeatherDTO
        for (city in cities) {
            val callbackOneCityWeather = object : MyLargeSuperCallback {
                override fun onResponse(weatherDTO: WeatherDTO) {
                    lastDTO = weatherDTO
                    city.temperature = weatherDTO.fact.temp
                    city.feelsLike = weatherDTO.fact.feelsLike
                    city.condition = WeatherCondition.getRusName(weatherDTO.fact.condition)
                    city.icon = weatherDTO.fact.icon
                    totalCounter++
                }

                override fun onFailure(e: IOException) {
                    Log.e("CityListRepositoryImpl", "load city list error")
                    totalCounter++
                }
            }
            repository.getWeather(city.city.lat, city.city.lon, callbackOneCityWeather)
        }

        // ждем завершения всех запросов по списку городов
        Thread {
            while (true) {
                if (totalCounter == cities.size) {
                    lastDTO?.let { vmCallback.onResponse(it) }
                    break
                }
            }
        }.start()

        return cities
    }
}