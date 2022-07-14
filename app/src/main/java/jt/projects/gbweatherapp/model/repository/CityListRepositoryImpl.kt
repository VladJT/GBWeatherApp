package jt.projects.gbweatherapp.model.repository

import android.util.Log
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import jt.projects.gbweatherapp.utils.WeatherCondition
import java.io.IOException

class CityListRepositoryImpl : CityListRepository {

    lateinit var allCitiesLoadedCallback: CityListLoadCallback

    override fun getCityList(choose: Location, callback: CityListLoadCallback): List<Weather> {
        allCitiesLoadedCallback = callback
        return when (choose) {
            Location.RUSSIAN -> getCityListFromLocalStorageRus()
            Location.WORLD -> getCityListFromLocalStorageWorld()
            else -> {
                getCityListFromLocalStorageRus()
            }
        }
    }

    private fun getCityListFromLocalStorageRus(): List<Weather> {
        return updateDataWithInternet(getRussianCities())
    }

    private fun getCityListFromLocalStorageWorld(): List<Weather> {
        return updateDataWithInternet(getWorldCities())
    }

    private fun updateDataWithInternet(cities: List<Weather>): List<Weather> {
        val repository = RepositoryDetailsOkHttpImpl()
        var totalCounter = 0
        for (city in cities) {
            val callbackOneCityWeather = object : WeatherDTOLoadCallback {
                override fun onResponse(weatherDTO: WeatherDTO) {
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
                    allCitiesLoadedCallback.onResponse()
                    break
                }
            }
        }.start()

        return cities
    }
}