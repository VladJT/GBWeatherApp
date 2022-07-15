package jt.projects.gbweatherapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class FavoritesViewModel : ViewModel() {
    private val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    private val repository : RepositoryAllWeather = RepositoryRoomImpl()

    fun getLiveData(): LiveData<AppState<List<Weather>>> = liveData

    fun getWeather() {
        liveData.value = AppState.Loading
        repository.getWeatherAll(callback)
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