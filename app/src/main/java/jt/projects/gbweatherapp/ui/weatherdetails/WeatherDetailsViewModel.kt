package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.repository.*
import jt.projects.gbweatherapp.viewmodel.DTOAppState
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"


class WeatherDetailsViewModel : ViewModel() {

    val liveData: MutableLiveData<DTOAppState> = MutableLiveData()
    lateinit var repository: RepositoryDetails

    fun getDetailsLiveData(): MutableLiveData<DTOAppState> {
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
        liveData.value = DTOAppState.Loading
        repository.getWeather(lat, lon, callback)
    }

    private val callback = object : MyLargeSuperCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            liveData.postValue(DTOAppState.Success(weatherDTO))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DTOAppState.Error(e))
        }
    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() { // TODO HW ***
        super.onCleared()
    }
}