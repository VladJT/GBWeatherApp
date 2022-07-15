package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.utils.NetworkChangeReceiver
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class WeatherDetailsViewModel : ViewModel() {
    val liveData: MutableLiveData<AppState<WeatherDTO>> = MutableLiveData()
    lateinit var repository: RepositoryDetails
    lateinit var repositoryAppendable: RepositoryAppendable

    fun getDetailsLiveData(): MutableLiveData<AppState<WeatherDTO>> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        if (!isConnection()) {
            repository = RepositoryDetailsRoomImpl()
        } else {
            repository = when (2) {
                1 -> {
                    RepositoryDetailsOkHttpImpl()
                }
                2 -> {
                    RepositoryDetailsRetrofitImpl()
                }
                3 -> {
                    RepositoryDetailsWeatherLoaderImpl()
                }
                else -> {
                    //    RepositoryDetailsLocalImpl()
                    RepositoryDetailsRetrofitImpl()
                }
            }
        }

        repositoryAppendable = RepositoryDetailsRoomImpl()
    }

    fun getWeather(city: City) {
        choiceRepository()
        liveData.value = AppState.Loading
        repository.getWeather(city, callback)
    }

    private val callback = object : WeatherLoadCallback {
        override fun onResponse(weather: Weather) {
            liveData.postValue(AppState.Success(weather))
            if (isConnection()) repositoryAppendable.addWeather(weather)// сохраняем историю
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