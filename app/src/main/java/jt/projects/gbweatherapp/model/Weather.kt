package jt.projects.gbweatherapp.model


data class Weather(
    val weatherData: WeatherData,// информация о текущей погоде
    val city: City,//информация о населенном пункте
    val forecasts: List<WeatherData>// Прогноз погоды на ближайшие 5 дней
)