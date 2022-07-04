package jt.projects.gbweatherapp.model

class RepositoryImpl : Repository {
    override fun getWeatherFromInternet(): Weather {
        return getRussianCities()[0]
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }
}