package jt.projects.gbweatherapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = """Фрагмент *ПОИСК*
            |
            |Здесь будет осуществляться поиск погоды по вводимым параметрам
            |Найденный город можно будет добавлять в список Избранного или устанавливать как стартовый город
            |Сведения о стартовом городе и городах в Избранном будут хранится в shared_pref (или DataStore)
            |
            |Функционал: 
            |1. ввод параметров + поиск
            |2. добавление в Избранное
            |3. задать найденный город как стартовый
        """.trimMargin()
    }
    val text: LiveData<String> = _text
}