package jt.projects.gbweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.CityListLoadCallback
import jt.projects.gbweatherapp.model.repository.CityListRepository
import jt.projects.gbweatherapp.model.repository.CityListRepositoryImpl
import jt.projects.gbweatherapp.model.repository.Location
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    private val cityListRepositoryImpl: CityListRepository = CityListRepositoryImpl()

    fun getData(): LiveData<AppState<List<Weather>>> = liveData
    lateinit var cities: List<Weather>

    fun getData(isRussian: Boolean) {
        liveData.value = AppState.Loading
        cities = if (isRussian)
            cityListRepositoryImpl.getCityList(Location.RUSSIAN, callback) else
            cityListRepositoryImpl.getCityList(Location.WORLD, callback)
    }

    private val callback = object : CityListLoadCallback {
        override fun onResponse() {
            liveData.postValue(AppState.Success(cities))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(Throwable("Ошибка загрузки списка городов")))
        }
    }

}