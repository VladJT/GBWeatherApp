package jt.projects.gbweatherapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import jt.projects.gbweatherapp.viewmodel.AppState


class SearchViewModel : ViewModel() {
    val counter: MutableLiveData<Int> = MutableLiveData()

    init {
        Thread {
            for (i in 1..100000) {
                counter.postValue(i)
                // Thread.sleep((500..1500).random().toLong())
                Thread.sleep(500)
            }
        }.start()
    }
}