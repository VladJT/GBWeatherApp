package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.ReposityHistoryWeatherImpl
import jt.projects.gbweatherapp.model.repository.WeatherListLoadCallback
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class HistoryDetailsViewModel : ViewModel() {
    val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    lateinit var repositoryHistory: ReposityHistoryWeatherImpl

    fun getDetailsLiveData(): MutableLiveData<AppState<List<Weather>>> {
        repositoryHistory = ReposityHistoryWeatherImpl()
        return liveData
    }

    fun addHistory(weather: Weather) {
        repositoryHistory.addWeather(weather)
    }

    fun getHistoryByLocation(city: City) {
        liveData.value = AppState.Loading
        repositoryHistory.getHistoryByLocation(city, callback)
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