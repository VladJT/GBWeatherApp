package jt.projects.gbweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException


class HomeViewModel : ViewModel() {
    private val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    private val repositoryCityList: RepositoryCityList = RepositoryCityListImpl()
    private val repositoryWeatherEdit: WeatherEditable = RepositoryRoomImpl()


    fun getLiveData(): LiveData<AppState<List<Weather>>> = liveData
    private var cities: List<Weather> = listOf()

    fun deleteFromFavorites(weather: Weather) {
        repositoryWeatherEdit.deleteAllWeatherByLocation(weather.city)
    }

    fun addToFavorites(weather: Weather) {
        repositoryWeatherEdit.addWeather(weather)
    }

    fun getCityList(isRussian: Boolean) {
        liveData.value = AppState.Loading
        cities = if (isRussian)
            repositoryCityList.getCityList(Location.RUSSIAN, callback) else
            repositoryCityList.getCityList(Location.WORLD, callback)
    }

    private val callback = object : CommonCallback {
        override fun onResponse() {
            liveData.postValue(AppState.Success(cities))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(Throwable("Ошибка загрузки списка городов")))
        }
    }
}