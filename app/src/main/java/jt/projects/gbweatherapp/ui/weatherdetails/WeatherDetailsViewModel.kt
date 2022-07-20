package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.utils.NetworkChangeReceiver
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class WeatherDetailsViewModel : ViewModel() {
    val liveData: MutableLiveData<AppState<Weather>> = MutableLiveData()
    lateinit var repository: RepositoryWeather
    var currentDate: Long = 0

    fun getDetailsLiveData(): MutableLiveData<AppState<Weather>> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        if (!isConnection()) {
            repository = RepositoryRoomImpl()
        } else {
            repository = when (2) {
                1 -> {
                    RepositoryOkHttpImpl()
                }
                2 -> {
                    RepositoryRetrofitImpl()
                }
                3 -> {
                    RepositoryStreamLoaderImpl()
                }
                else -> {
                    RepositoryRetrofitImpl()
                }
            }
        }
    }


    fun getWeather(city: City) {
        liveData.value = AppState.Loading
        repository.getWeather(city, callback)
    }

    private val callback = object : WeatherLoadCallback {
        override fun onResponse(weather: Weather?) {
            if (weather != null) {
                liveData.postValue(AppState.Success(weather))
                currentDate = weather.now
            } else throw Exception("weather==null for WeatherDetailsFragment")
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(e))
        }
    }

    private fun isConnection(): Boolean {
        return NetworkChangeReceiver.isConnected
    }

    override fun onCleared() { // TODO HW ***
        super.onCleared()
    }
}