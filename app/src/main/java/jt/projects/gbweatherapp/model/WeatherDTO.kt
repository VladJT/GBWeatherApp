package jt.projects.gbweatherapp.model


data class WeatherDTO(
    val now: String,//Дата  в UTC
    val now_dt: String, //Дата в Unixtime
    val fact: WeatherData,// информация о текущей погоде
    val city: City,//информация о населенном пункте
    val forecasts: List<WeatherData>?// Прогноз погоды на ближайшие x дней
)


//fun getTestWeather() = WeatherDTO(
//    WeatherData("2022-06-25", 1470220206, 20, 21, "ovc", "overcast", 2.0, 745, 54, 1, 0.25),
//    City("Москва", 55.833333, 37.616667), null
//)


//fun getWorldCities(): List<WeatherDTO> {
//    return listOf(
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 1, 2, "ovc", "cloudy", 2.0, 745, 54, 1, 0.25),
//            City("Лондон", 51.5085300, -0.1257400), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 3, 4, "ovc", "clear", 2.0, 745, 54, 1, 0.25),
//            City("Токио", 35.6895000, 139.6917100), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 5, 6, "ovc", "overcast", 2.0, 745, 54, 1, 0.25),
//            City("Париж", 48.8534100, 2.3488000), null
//        )
//    )
//}
//
//fun getRussianCities(): List<WeatherDTO> {
//    return listOf(
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 20, 21, "ovc", "cloudy", 2.0, 745, 54, 1, 0.25),
//            City("Москва", 55.833333, 37.616667), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 1, 2, "ovc", "clear", 2.0, 745, 54, 1, 0.25),
//            City("Воронеж", 51.5085300, -0.1257400), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 3, 4, "ovc", "cloudy", 2.0, 745, 54, 1, 0.25),
//            City("Мурманск", 35.6895000, 139.6917100), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 5, 6, "ovc", "overcast", 2.0, 745, 54, 1, 0.25),
//            City("Самара", 48.8534100, 2.3488000), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 25, 24, "ovc", "cloudy", 2.0, 745, 54, 1, 0.25),
//            City("Белгород", 15.833333, 7.616667), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 16, 12, "ovc", "clear", 2.0, 745, 54, 1, 0.25),
//            City("Орел", 31.5085300, 10.1257400), null
//        ),
//        WeatherDTO(
//            WeatherData("2022-06-25", 1470220206, 23, 24, "ovc", "cloudy", 2.0, 745, 54, 1, 0.25),
//            City("Сочи", 35.6895000, 139.6917100), null
//        )
//
//    )
//}