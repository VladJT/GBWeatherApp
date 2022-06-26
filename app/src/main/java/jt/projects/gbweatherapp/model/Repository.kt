package jt.projects.gbweatherapp.model

interface Repository {
    fun getWeatherFromInternet(): Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}