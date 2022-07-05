package jt.projects.gbweatherapp.model

data class WeatherDTO(
    val now: String,//Дата  в UTC
    val now_dt: String, //Дата в Unixtime
    val fact: WeatherData,// информация о текущей погоде
    val city: City,//информация о населенном пункте
    val forecasts: List<WeatherData>?// Прогноз погоды на ближайшие x дней
)