package jt.projects.gbweatherapp.viewmodel

import jt.projects.gbweatherapp.model.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class SuccessMulti(val weatherData: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}