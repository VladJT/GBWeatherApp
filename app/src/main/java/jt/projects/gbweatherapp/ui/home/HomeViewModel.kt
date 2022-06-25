package jt.projects.gbweatherapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = """Фрагмент *Мой город*
            |
            |Стартовый фрагмент, будет отображаться детальная погода по 1 городу
            |
            |Город будет задаваться как стартовый из фрагмента поиска (будет хранится в shared_pref)
            |
            |В нижней половине экрана в RecyclerView будет погода по часам (или какая-то иная списочная информация)
        """.trimMargin()
    }
    val text: LiveData<String> = _text
}