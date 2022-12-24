package jt.projects.gbweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City,
    var temperature: Int = 0,
    var feelsLike: Int = 0,
    var condition: String = "--",
    var icon: String = "ovc",
    val pressureMm: Double = 0.0,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    var now: Long = 0,
    val nowDt: String = "",
    var isExistInRoom: Boolean = false
) : Parcelable


fun getWorldCities(): List<Weather> {
    return listOf(
        Weather(City("Лондон", 51.5085300, -0.1257400)),
        Weather(City("Токио", 35.6895000, 139.6917100)),
        Weather(City("Париж", 48.8534100, 2.3488000)),
        Weather(City("Берлин", 52.52000659999999, 13.404953999999975)),
        Weather(City("Рим", 41.9027835, 12.496365500000024)),
        Weather(City("Минск", 53.90453979999999, 27.561524400000053)),
        Weather(City("Стамбул", 41.0082376, 28.97835889999999)),
        Weather(City("Вашингтон", 38.9071923, -77.03687070000001)),
        Weather(City("Киев", 50.4501, 30.523400000000038)),
        Weather(City("Пекин", 39.90419989999999, 116.40739630000007))
    )
}

fun getRussianCities(): List<Weather> {
    return listOf(
        Weather(City("Москва", 55.755826, 37.617299900000035)),
        Weather(City("Мурманск", 68.9792, 33.0925)),
        Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038)),
        Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002)),
        Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001)),
        Weather(City("Нижний Новгород", 56.2965039, 43.936059)),
        Weather(City("Казань", 55.8304307, 49.06608060000008)),
        Weather(City("Челябинск", 55.1644419, 61.4368432)),
        Weather(City("Омск", 54.9884804, 73.32423610000001)),
        Weather(City("Ростов-на-Дону", 47.2357137, 39.701505)),
        Weather(City("Уфа", 54.7387621, 55.972055400000045))
    )
}
