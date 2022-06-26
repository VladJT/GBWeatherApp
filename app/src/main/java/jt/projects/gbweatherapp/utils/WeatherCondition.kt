package jt.projects.gbweatherapp.utils

enum class WeatherCondition(var enName: String, var rusName: String) {
    CLEAR("clear", "ясно"),
    PARTLY_CLOUDY("partly-cloudy", "малооблачно"),
    CLOUDY("cloudy", "облачно с прояснениями"),
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