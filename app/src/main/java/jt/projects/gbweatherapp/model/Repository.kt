package jt.projects.gbweatherapp.model


interface Repository {
    fun getWeatherFromInternet(): Weather
    fun getWeatherFromLocalStorage(): Weather
}