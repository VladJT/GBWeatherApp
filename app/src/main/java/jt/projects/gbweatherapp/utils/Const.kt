package jt.projects.gbweatherapp.utils

// для HTTP-запросов
const val REQUEST_GET = "GET"
const val REQUEST_TIMEOUT = 2000
const val REQUEST_API_KEY = "X-Yandex-API-Key"
const val ICON_URL = "https://yastatic.net/weather/i/icons/blueye/color/svg/%s.svg"
const val REQUEST_URL = "https://api.weather.yandex.ru/v2/forecast?lat=%s&lon=%s"

// ключи ответа запросов HTTP
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
const val NOTIFICATION_CHANNEL_ID = "2"
const val NOTIFICATION_CHANNEL_NAME = "vlad"


