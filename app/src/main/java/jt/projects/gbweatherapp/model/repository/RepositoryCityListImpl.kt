package jt.projects.gbweatherapp.model.repository

import android.util.Log
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import java.io.IOException

class RepositoryCityListImpl : RepositoryCityList {

    private val lock = Any()
    lateinit var allCitiesLoadedCallback: CommonLoadCallback

    override fun getCityList(choose: Location, callback: CommonLoadCallback): List<Weather> {
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
        val repository = RepositoryOkHttpImpl()
        var totalCounter = 0
        for (city in cities) {
            val callbackOneCityWeather = object : WeatherLoadCallback {
                override fun onResponse(weather: Weather) {
                    city.temperature = weather.temperature
                    city.feelsLike = weather.feelsLike
                    city.condition = weather.condition
                    city.icon = weather.icon
                    city.city.lon = weather.city.lon
                    city.city.lat = weather.city.lat
                    city.city.name = weather.city.name
                    synchronized(lock) { totalCounter++ }
                }

                override fun onFailure(e: IOException) {
                    Log.e("RepositoryCityListImpl", "Ошибка загрузки списка городов")
                    synchronized(lock) { totalCounter++ }
                }
            }
            repository.getWeather(city.city, callbackOneCityWeather)
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