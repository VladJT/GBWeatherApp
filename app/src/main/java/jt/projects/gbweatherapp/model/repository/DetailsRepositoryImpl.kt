package jt.projects.gbweatherapp.model.repository

import jt.projects.gbweatherapp.model.dto.WeatherDTO
import okhttp3.Callback

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {
    override fun getWeatherDetailsFromServerOkHttp(requestLink: String, callback: Callback) {
        remoteDataSource.getWeatherDetailsWithOkHttp(requestLink, callback)
    }

    override fun getWeatherDetailsFromServerRetrofit(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetailsWithRetrofit(lat, lon, callback)
    }
}