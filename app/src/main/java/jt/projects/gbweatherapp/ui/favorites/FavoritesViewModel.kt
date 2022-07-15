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
    private val repository : RepositoryWeatherAll = RepositoryDetailsRoomImpl()

    fun getData(): LiveData<AppState<List<Weather>>> = liveData

    fun getAllWeather() {
        liveData.value = AppState.Loading
        //repository.getWeatherAll()
    }

    private val callback = object : AllWeatherLoadCallback {
        override fun onResponse(weatherList: List<Weather>) {
            liveData.postValue(AppState.Success(weatherList))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(Throwable("Ошибка загрузки из Room")))
        }
    }
}