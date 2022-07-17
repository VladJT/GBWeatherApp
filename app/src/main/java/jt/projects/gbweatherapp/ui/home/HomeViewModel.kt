package jt.projects.gbweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.CommonCallback
import jt.projects.gbweatherapp.model.repository.Location
import jt.projects.gbweatherapp.model.repository.RepositoryCityList
import jt.projects.gbweatherapp.model.repository.RepositoryCityListImpl
import jt.projects.gbweatherapp.viewmodel.AppState
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val liveData: MutableLiveData<AppState<List<Weather>>> = MutableLiveData()
    private val cityList: RepositoryCityList = RepositoryCityListImpl()

    fun getLiveData(): LiveData<AppState<List<Weather>>> = liveData
    lateinit var cities: List<Weather>

    fun getCityList(isRussian: Boolean) {
        liveData.value = AppState.Loading
        cities = if (isRussian)
            cityList.getCityList(Location.RUSSIAN, callback) else
            cityList.getCityList(Location.WORLD, callback)
    }

    private val callback = object : CommonCallback {
        override fun onResponse() {
            liveData.postValue(AppState.Success(cities))
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(AppState.Error(Throwable("Ошибка загрузки списка городов")))
        }
    }
}