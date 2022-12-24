package jt.projects.gbweatherapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.RepositoryRoomImpl
import jt.projects.gbweatherapp.model.repository.RepositoryWeatherList
import jt.projects.gbweatherapp.model.repository.WeatherEditable
import jt.projects.gbweatherapp.model.repository.WeatherListLoadCallback
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException


class FavoritesViewModel : ViewModel() {
    private val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    private val repositoryWeatherEdit: WeatherEditable = RepositoryRoomImpl()
    private val repositoryWeatherList: RepositoryWeatherList = RepositoryRoomImpl()

    fun getLiveData(): LiveData<AppState<List<Weather>>> = liveData

    fun getWeather() {
        liveData.value = AppState.Loading
        repositoryWeatherList.getWeatherList(callback)
    }

    fun deleteFromFavorites(weather: Weather) {
        repositoryWeatherEdit.deleteAllWeatherByLocation(weather.city)
    }

    fun addToFavorites(weather: Weather) {
        repositoryWeatherEdit.addWeather(weather)
    }

    private val callback = object : WeatherListLoadCallback {
        override fun onResponse(weather: List<Weather>) {
            liveData.postValue(AppState.Success(weather))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(Throwable("Ошибка загрузки из Room")))
        }
    }
}