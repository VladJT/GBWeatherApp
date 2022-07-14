package jt.projects.gbweatherapp.model.repository

fun interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double, callback: WeatherDTOLoadCallback)
}