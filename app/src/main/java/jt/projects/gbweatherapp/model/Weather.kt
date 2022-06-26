package jt.projects.gbweatherapp.model

data class Weather(
    val weatherData: WeatherData,// информация о текущей погоде
    val city: City,//информация о населенном пункте
    val forecasts: List<WeatherData>?// Прогноз погоды на ближайшие 5 дней
)


fun getTestWeather() = Weather(
    WeatherData("2022-06-25", 1470220206, 20, 21, "ovc", "overcast", 2.0, 745,54, 1, 0.25),
    City("Москва", 55.833333, 37.616667),
    null)


fun getTestWeather2() = Weather(
    WeatherData("2022-06-25", 1470220206, 15, 13, "ovc", "overcast", 2.5, 795,56, 2, 0.5),
    City("Мурманск", 40.833333, 88.616667),
    null)