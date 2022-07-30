package jt.projects.gbweatherapp.viewmodel

sealed class AppState<out T> {
    data class Success<T>(val data: T) : AppState<Nothing>()
    data class Error(val error: Throwable) : AppState<Nothing>()
    object Loading : AppState<Nothing>()
}