package jt.projects.gbweatherapp.model


class RepositoryImpl : Repository {
    override fun getWeatherFromInternet(): Weather {
//        val t = Thread {
//            Thread.sleep(1000)
//        }
//        t.start()
//        t.join()
        return getTestWeather()
    }

    override fun getWeatherFromLocalStorage(): Weather {
        return getTestWeather2()
    }
}