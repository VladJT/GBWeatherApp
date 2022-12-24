package jt.projects.gbweatherapp.utils

// LOGS
const val TAG = "@@@"

// для HTTP-запросов
const val REQUEST_GET = "GET"
const val REQUEST_TIMEOUT = 2000
const val REQUEST_API_KEY = "X-Yandex-API-Key"
const val ICON_URL = "https://yastatic.net/weather/i/icons/blueye/color/svg/%s.svg"
const val REQUEST_URL = "https://api.weather.yandex.ru/v2/informers?lat=%s&lon=%s"

const val BASE_URL = "https://api.weather.yandex.ru/"
const val BASE_URL_PART_TWO = "v2/informers"


// ключи ответа запросов HTTP
const val SERVER_ERROR = "Ошибка сервера"
const val REQUEST_ERROR = "Ошибка запроса на сервер"
const val CORRUPTED_DATA = "Неполные данные"

const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "Интент пуст"
const val DETAILS_DATA_EMPTY_EXTRA = "Нет данных"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "Ответ пуст"
const val DETAILS_REQUEST_ERROR_EXTRA = "Ошибка ответа"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "Детализация ошибки ответа"
const val DETAILS_URL_MALFORMED_EXTRA = "Некорректный URL(URI)"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "Успешный ответ"

// анимации VIEW
const val DURATION_ITEM_ANIMATOR: Long = 300

// настройки канала PUSH-уведомлений
const val NOTIFICATION_CHANNEL_ID = "PUSH"
const val NOTIFICATION_CHANNEL_NAME = "vlad"

// ROOM
const val ROOM_DB_NAME_WEATHER = "weather.db"

// FireStore Cloud Messaging
const val NOTIFICATION_KEY_TITLE = "myTitle"
const val NOTIFICATION_KEY_MESSAGE = "myBody"

// NOTIFICATIONS
const val MY_GROUP_ID = "Каналы для GBWeatherApp"
const val CHANNEL_HIGH_ID = "channel_high"
const val CHANNEL_LOW_ID = "channel_low"
const val NOTIFICATION_ID = 1
const val NOTIFICATION_ID2 = 2

const val SHOW_WEATHER_DETAILS_INTENT = "SHOW_WEATHER_DETAILS_INTENT"

//Fragments TAGS
const val FRAGMENT_TAG_MAP = "FRAGMENT_TAG_MAP"
const val CONTACTS_FRAGMENT_TAG = "CONTACTS_FRAGMENT_TAG"
const val WEATHER_DETAILS_FRAGMENT_TAG = "WEATHER_DETAILS_FRAGMENT_TAG"
