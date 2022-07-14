package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class WeatherDetailsViewModel : ViewModel() {
    val liveData: MutableLiveData<AppState<WeatherDTO>> = MutableLiveData()
    lateinit var repository: RepositoryDetails

    fun getDetailsLiveData(): MutableLiveData<AppState<WeatherDTO>> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
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

    fun getWeather(lat: Double, lon: Double) {
        choiceRepository()
        liveData.value = AppState.Loading
        repository.getWeather(lat, lon, callback)
    }

    private val callback = object : WeatherDTOLoadCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            liveData.postValue(AppState.Success(weatherDTO))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(e))
        }
    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() { // TODO HW ***
        super.onCleared()
    }
}