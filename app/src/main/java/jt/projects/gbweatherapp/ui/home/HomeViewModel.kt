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

    fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading

        val result = if (isRussian)
            repositoryImpl.getWeatherFromLocalStorageRus() else
            repositoryImpl.getWeatherFromLocalStorageWorld()

        when ((0..5).random()) {
            0,1,2,3,4 -> {
                Thread {
                    Thread.sleep(200)
                    liveDataToObserve.postValue(AppState.SuccessMulti(result))
                }.start()
            }
            5 -> {
                Thread {
                    Thread.sleep(500)
                    liveDataToObserve.postValue(AppState.Error(Throwable("Не удалось получить данные")))
                }.start()
            }
        }
    }

}