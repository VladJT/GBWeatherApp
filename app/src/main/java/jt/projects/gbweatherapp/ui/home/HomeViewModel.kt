package jt.projects.gbweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.repository.CityListRepository
import jt.projects.gbweatherapp.model.repository.CityListRepositoryImpl
import jt.projects.gbweatherapp.model.repository.MyLargeSuperCallback
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val cityListRepositoryImpl: CityListRepository = CityListRepositoryImpl()

    fun getData(): LiveData<AppState> = liveDataToObserve
    lateinit var cities: List<Weather>

    fun getData(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        cities = if (isRussian)
            cityListRepositoryImpl.getCityList(2, callback) else
            cityListRepositoryImpl.getCityList(3, callback)
    }

    private val callback = object : MyLargeSuperCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            liveDataToObserve.postValue(AppState.SuccessMulti(cities))
        }

        override fun onFailure(e: IOException) {
            liveDataToObserve.postValue(AppState.Error(Throwable("city list total error")))
        }
    }

}