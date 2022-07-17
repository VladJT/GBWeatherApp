package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class HistoryDetailsViewModel : ViewModel() {
    val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()

    lateinit var repository: RepositoryHistoryWeather

    fun getDetailsLiveData(): MutableLiveData<AppState<List<Weather>>> {
        repository = ReposityHistoryWeatherImpl()
        return liveData
    }

    fun addHistory(weather: Weather) {
        repository.addWeather(weather)
    }

    fun getHistoryByLocation(city: City) {
        liveData.value = AppState.Loading
        repository.getHistoryByLocation(city, callback)
    }

    private val callback = object : WeatherListLoadCallback {
        override fun onResponse(weather: List<Weather>) {
            liveData.postValue(AppState.Success(weather))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(Throwable("Ошибка загрузки History из Room")))
        }
    }
}