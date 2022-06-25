package jt.projects.gbweatherapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoritesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = """Фрагмент *Избранное*
            |
            |Здесь будет список городов с погодой, добавленных в избранное на экране поиска
            |Режим просмотра - RecyclerView Vertical
            |
            |Функционал - удаление из избранного
        """.trimMargin()
    }
    val text: LiveData<String> = _text
}