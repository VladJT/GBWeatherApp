package jt.projects.gbweatherapp.model.weatherdetails

import okhttp3.Callback

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource)  : DetailsRepository {
    override fun getWeatherDetailsFromServer(requestLink: String, callback: Callback) {
        remoteDataSource.getWeatherDetails(requestLink, callback)
    }
}