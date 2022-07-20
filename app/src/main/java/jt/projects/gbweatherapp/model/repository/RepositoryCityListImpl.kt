package jt.projects.gbweatherapp.model.repository

import android.util.Log
import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import jt.projects.gbweatherapp.model.room.WeatherEntity
import java.io.IOException

class RepositoryCityListImpl : RepositoryCityList {

    private val lock = Any()
    lateinit var allCitiesLoadedCallback: CommonCallback

    override fun getCityList(choose: Location, callback: CommonCallback): List<Weather> {
        allCitiesLoadedCallback = callback
        return when (choose) {
            Location.RUSSIAN -> getCityListFromLocalStorageRus()
            Location.WORLD -> getCityListFromLocalStorageWorld()
            else -> {
                getCityListFromLocalStorageRus()
            }
        }
    }

    override fun getWeatherByLocation(
        city: City,
        callback: WeatherListLoadCallback
    ) {
        Thread {
            val responce = convertToWeather(
                MyApp.getWeatherDbAsyncMode().weatherDao().getWeatherByLocation(city.lat, city.lon)
            )
            callback.onResponse(responce)
        }.start()
    }

    private fun convertToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City(it.name, it.lat, it.lon),
                temperature = it.temperature,
                feelsLike = it.feelsLike,
                condition = it.condition,
                icon = it.icon,
                now = it.now
            )
        }
    }

    private fun getCityListFromLocalStorageRus(): List<Weather> {
        return updateDataWithInternet(getRussianCities())
    }

    private fun getCityListFromLocalStorageWorld(): List<Weather> {
        return updateDataWithInternet(getWorldCities())
    }

    private fun updateDataWithInternet(cityList: List<Weather>): List<Weather> {
        var result = mutableListOf<Weather>()
        val repository = RepositoryOkHttpImpl()
        var totalCounter = 0
        for (city in cityList) {
            val callbackOneCityWeather = object : WeatherLoadCallback {
                override fun onResponse(weather: Weather?) {
                    synchronized(lock) {
                        weather?.let { result.add(it) }
                        totalCounter++
                    }
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
                if (totalCounter == cityList.size) {
                    result.sortBy { it.city.name }
                    allCitiesLoadedCallback.onResponse()
                    break
                }
            }
        }.start()

        return result
    }
}