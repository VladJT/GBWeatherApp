package jt.projects.gbweatherapp.viewmodel

import jt.projects.gbweatherapp.model.Weather


sealed class AppState {
    data class Success(val weather: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}