package jt.projects.gbweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.model.Repository
import jt.projects.gbweatherapp.model.RepositoryImpl
import jt.projects.gbweatherapp.viewmodel.AppState

class HomeViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repositoryImpl: Repository = RepositoryImpl()


    fun getData(): LiveData<AppState> = liveDataToObserve


    fun loadWeather() {
        liveDataToObserve.value = AppState.Loading

        val result = repositoryImpl.getWeatherFromInternet()

        when ((0..1).random()) {
            0 -> {
                Thread {
                    Thread.sleep(200)
                    liveDataToObserve.postValue(AppState.Success(result))
                }.start()
            }
            1 -> {
                Thread {
                    Thread.sleep(500)
                    liveDataToObserve.postValue(AppState.Error(Throwable("Не удалось получить данные")))
                }.start()
            }
        }
    }

    fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading
        Thread {
            Thread.sleep(1000)
            liveDataToObserve.postValue(AppState.SuccessMulti(if (isRussian)
                repositoryImpl.getWeatherFromLocalStorageRus() else
                repositoryImpl.getWeatherFromLocalStorageWorld()))
        }.start()
    }

}