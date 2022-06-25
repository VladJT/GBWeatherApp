package jt.projects.gbweatherapp.model

/*
lat	Широта (в градусах).	Число
lon	Долгота (в градусах).	Число
tzinfo	Информация о часовом поясе. Содержит поля offset, name, abbr и dst.	Объект
offset	Часовой пояс в секундах от UTC.	Число
name	Название часового пояса.	Строка
abbr	Сокращенное название часового пояса.	Строка
dst	Признак летнего времени.	Логический
def_pressure_mm	Норма давления для данной координаты (в мм рт. ст.).	Число
def_pressure_pa	Норма давления для данной координаты (в гектопаскалях).	Число
url	Страница населенного пункта на сайте Яндекс.Погода.	Строка

"info": {
    "lat": 55.833333,
    "lon": 37.616667,
    "tzinfo": {
      "offset": 10800,
      "name": "Europe/Moscow",
      "abbr": "MSK",
      "dst": false
    },
    "def_pressure_mm": 746,
    "def_pressure_pa": 994,
    "url": "https://yandex.ru/pogoda/moscow"
  }
 */
data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)