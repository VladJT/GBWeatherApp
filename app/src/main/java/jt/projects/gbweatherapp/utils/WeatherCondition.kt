package jt.projects.gbweatherapp.utils

enum class WeatherCondition(var enName: String, var rusName: String) {
    CLEAR("clear", "ясно"),
    PARTLY_CLOUDY("partly-cloudy", "малооблачно"),
    CLOUDY("cloudy", "облачно с прояснениями"),
    RAIN("rain", "дождь"),
    LIGHT_RAIN("light-rain", "небольшой дождь"),
    OVERCAST("overcast", "облачно");
    companion object {
        fun getRusName(name: String): String {
            for (condition in values()) {
                if (condition.enName.equals(name)) {
                    return condition.rusName
                }
            }
            return name
        }
    }
}

fun getPrecType(code: Int): String {
    return when (code) {
        0 -> "без осадков"
        1 -> "дождь"
        2 -> "дождь со снегом"
        3 -> "снег"
        4 -> "град"
        else -> "без осадков"
    }
}