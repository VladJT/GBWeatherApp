package jt.projects.gbweatherapp.utils

import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.Fact
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.room.WeatherEntity
import jt.projects.gbweatherapp.model.room.WeatherHistoryEntity


fun String.toTemperature(): String {
    val i: Int = this.toInt()
    return if (i > 0) "+${this}\u2103" else "${this}\u2103"
}

fun Long.toDateTime(): String {
    return java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(java.util.Date(this * 1000))
}

fun convertDTOtoModel(weatherDTO: WeatherDTO, city: City): Weather {
    val fact = weatherDTO.fact
    return Weather(
        city = city,
        temperature = fact.temp,
        feelsLike = fact.feelsLike,
        condition = fact.condition,
        icon = fact.icon,
        pressureMm = fact.pressureMm,
        humidity = fact.humidity,
        windSpeed = fact.windSpeed,
        now = weatherDTO.now,
        nowDt = weatherDTO.nowDt
    )
}

fun convertModelToDto(weather: Weather): WeatherDTO {
    return WeatherDTO(
        Fact(
            temp = weather.temperature,
            feelsLike = weather.feelsLike,
            icon = weather.icon,
            condition = weather.condition,
            pressureMm = weather.pressureMm,
            humidity = weather.humidity,
            windSpeed = weather.windSpeed
        ),
        now = weather.now,
        nowDt = weather.nowDt
    )
}

fun convertWeatherToEntity(weather: Weather): WeatherEntity {
    return WeatherEntity(
        0, weather.city.name, weather.city.lat, weather.city.lon,
        weather.temperature, weather.feelsLike, weather.condition, weather.icon, weather.now
    )
}

fun convertWeatherToHistory(weather: Weather): WeatherHistoryEntity {
    return WeatherHistoryEntity(
        0,
        weather.city.lat,
        weather.city.lon,
        weather.temperature,
        weather.now
    )
}
