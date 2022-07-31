package jt.projects.gbweatherapp.model.repository

import android.util.Log
import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.getRussianCities
import jt.projects.gbweatherapp.model.getWorldCities
import jt.projects.gbweatherapp.utils.TAG
import java.io.IOException
import java.util.concurrent.CountDownLatch

class RepositoryCityListImpl : RepositoryCityList {

    private val lock = Any()
    lateinit var allCitiesLoadedCallback: CommonCallback

    override fun getCityList(choose: Location, callback: CommonCallback): List<Weather> {
        allCitiesLoadedCallback = callback
        return when (choose) {
            Location.RUSSIAN -> getCityListFromLocalStorageRus()
            Location.WORLD -> getCityListFromLocalStorageWorld()
        }
    }


    // TODO подумать как переделать в фоновый поток
    fun isExistInRoom(city: City): Boolean {
        val cityList = MyApp.getWeatherDbMainThreadMode().weatherDao()
            .getWeatherByLocation(city.lat, city.lon)
        return cityList.isNotEmpty()
    }

    private fun getCityListFromLocalStorageRus(): List<Weather> {
        try {
            return updateDataWithInternet(getRussianCities())
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
        allCitiesLoadedCallback.onResponse()
        return getRussianCities()
    }

    private fun getCityListFromLocalStorageWorld(): List<Weather> {
        try {
            return updateDataWithInternet(getWorldCities())
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
        allCitiesLoadedCallback.onResponse()
        return getWorldCities()
    }


    private fun updateDataWithInternet(cityList: List<Weather>): List<Weather> {
        var result = mutableListOf<Weather>()
        val repository = RepositoryRetrofitImpl()
        // CountDownLatch позволяет потоку ожидать завершения операций, выполняющихся в других потоках.
        // Режим ожидания запускается методом await(). При создании объекта определяется количество
        // требуемых операций, после чего уменьшается при вызове метода countDown(). Как только счетчик
        // доходит до нуля, с ожидающего потока снимается блокировка.
        var cdl = CountDownLatch(cityList.size)
        for (city in cityList) {
            val callbackOneCityWeather = object : WeatherLoadCallback {
                override fun onResponse(weather: Weather?) {
                    synchronized(lock) {
                        weather?.let {
                            it.isExistInRoom = isExistInRoom(weather.city)
                            result.add(it)
                        }
                        cdl.countDown()
                    }
                }

                override fun onFailure(e: IOException) {
                    Log.e("RepositoryCityListImpl", "Ошибка загрузки списка городов")
                    synchronized(lock) {
                        cdl.countDown()
                    }
                }
            }
            repository.getWeather(city.city, callbackOneCityWeather)
        }

        Thread {
            cdl.await()// ждем завершения всех запросов по списку городов
            result.sortBy { it.city.name }
            synchronized(lock) {
                allCitiesLoadedCallback.onResponse()
            }
        }.start()
        return result
    }
}