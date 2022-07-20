package jt.projects.gbweatherapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.RepositoryRoomImpl
import jt.projects.gbweatherapp.model.repository.WeatherListLoadCallback
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException


class FavoritesViewModel : ViewModel() {
    private val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    private val repository = RepositoryRoomImpl()

    fun getLiveData(): LiveData<AppState<List<Weather>>> = liveData

    fun getWeather() {
        liveData.value = AppState.Loading
        repository.getWeatherList(callback)
    }

    fun deleteFromFavorites(weather: Weather) {
        repository.deleteAllWeatherByLocation(weather.city)
    }

    fun addToFavorites(weather: Weather) {
        repository.addWeather(weather)
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