package jt.projects.gbweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherData(
    val dateOfUpdate: String,//Дата  в UTC
    val date_ts: Int, //Дата в Unixtime
    val temperature: Int,//temp	Температура (°C).	Число
    val feelsLike: Int,//feels_like	Ощущаемая температура (°C).	Число
    val icon: String,//icon	Код иконки погоды. Иконка доступна по адресу https://yastatic.net/weather/i/icons/funky/dark/<значение из поля icon>.svg.	Строка
    val condition: String,//condition  Код расшифровки погодного описания. Возможные значения:clear — ясно..... Строка
    val wind_speed: Double,//wind_speed	Скорость ветра (в м/с).	Число
    val pressure_mm: Int,//pressure_mm	Давление (в мм рт. ст.).	Число
    val humidity: Int,//	Влажность воздуха (в процентах).
    val prec_type: Int,//prec_type	Тип осадков. Возможные значения:0 — без осадков.1 — дождь.2 — дождь со снегом.3 — снег.4 — град.Число
    val prec_strength: Double,//prec_strength	Сила осадков. Возможные значения:0 — без осадков.0.25 — слабый дождь/слабый снег.0.5 — дождь/снег.0.75 — сильный дождь/сильный снег.1 — сильный ливень/очень сильный снег.Число
) : Parcelable
