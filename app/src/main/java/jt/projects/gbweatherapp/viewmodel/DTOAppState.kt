package jt.projects.gbweatherapp.viewmodel

import jt.projects.gbweatherapp.model.dto.WeatherDTO


sealed class DTOAppState {
    data class Success(val weather: WeatherDTO) : DTOAppState()
    data class Error(val error: Throwable) : DTOAppState()
    object Loading : DTOAppState()
}