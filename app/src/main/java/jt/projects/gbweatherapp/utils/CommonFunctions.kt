package jt.projects.gbweatherapp.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.dto.Fact
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.room.WeatherEntity
import java.io.BufferedReader
import java.util.stream.Collectors

// конвертеры
fun convertDTOtoModel(weatherDTO: WeatherDTO, city: City): Weather {
    val fact = weatherDTO.fact
    return Weather(
        city,
        fact.temp,
        fact.feelsLike,
        fact.condition,
        fact.icon,
        fact.pressureMm,
        fact.humidity,
        fact.precType,
        fact.windSpeed,
        weatherDTO.now,
        weatherDTO.nowDt
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
            precType = weather.precType,
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

// принимает текст для вывода или как строку, или как id Resources (String)
fun <T> View.showSnackBarShort(text: T) {
    Snackbar.make(this, this.getUniString(text), Snackbar.LENGTH_SHORT).show()
}

// принимает текст для вывода SnackBar или как строку, или как id Resources (String)
fun <T, R> View.showSnackBarWithAction(text: T, actionText: R, action: () -> Unit) {
    Snackbar.make(
        this,
        this.getUniString(text),
        Snackbar.LENGTH_INDEFINITE
    )// отображается неопределенное время
        .setAction(this.getUniString(actionText)) { action.invoke() }
        .show()
}

fun <T> View.getUniString(text: T): String {
    return ((text as? Int)?.let { resources.getText(text as Int) } ?: text) as String
}

// Расширяем функционал View для отображения клавиатуры
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

// Расширяем функционал View для скрытия клавиатуры
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

// для WebView
@RequiresApi(Build.VERSION_CODES.N)
fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}

fun String.toTemperature(): String {
    val i: Int = this.toInt()
    return if (i > 0) "+${this}\u2103" else "${this}\u2103"
}

fun getDateFromUnixTime(date: Long): String {
    return java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(java.util.Date(date * 1000))
}