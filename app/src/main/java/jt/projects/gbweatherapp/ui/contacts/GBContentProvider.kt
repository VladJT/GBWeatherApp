package jt.projects.gbweatherapp.ui.contacts

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.model.room.WeatherHistoryEntity

private const val URI_ALL = 1 // URI для всех записей
private const val URI_ID = 2 // URI для конкретной записи
private const val ENTITY_PATH = "weather_history_table"
private const val ID = "id"
private const val LAT = "lat"
private const val LON = "lon"
private const val TEMPERATURE = "temperature"

class GBContentProvider : ContentProvider() {
    private var authorities: String? = null // Адрес URI
    private lateinit var uriMatcher: UriMatcher // Помогает определить тип адреса URI


    // Типы данных
    private var entityContentType: String? = null // Набор строк
    private var entityContentItemType: String? = null // Одна строка
    private lateinit var contentUri: Uri // Адрес URI Provider

    override fun onCreate(): Boolean {
        authorities = context?.getString(R.string.authorities)
        // Вспомогательный класс для определения типа запроса
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        // Если нас интересуют все объекты
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)
        // Если нас интересует только один объект
        uriMatcher.addURI(authorities, "$ENTITY_PATH/#", URI_ID)
        // Тип содержимого — все объекты
        entityContentType =
            "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"

        // Тип содержимого — один объект
        entityContentItemType =
            "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"
        // Строка для доступа к Provider
        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")
        return true
    }

    // Provider требует переопределения этого метода, чтобы понимать тип запроса
    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }
        return null
    }


    // Переводим ContentValues в HistoryEntity
    private fun convertToWeatherHistoryEntity(values: ContentValues?): WeatherHistoryEntity {
        return if (values == null) {
            WeatherHistoryEntity(0, 0.0, 0.0, 0, 0)
        } else {
            val id = if (values.containsKey(ID)) values[ID] as Long else 0
            val lat = values[LAT] as Double
            val lon = values[LON] as Double
            val temperature = values[TEMPERATURE] as Int
            WeatherHistoryEntity(id, lat, lon, temperature, 0)
        }
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        var historyDao = MyApp.getWeatherDbMainThreadMode().weatherDao()
        // При помощи UriMatcher определяем, запрашиваются все элементы или   один
        val cursor = when (uriMatcher.match(uri)) {
            URI_ALL -> historyDao.getHistoryCursor()
            URI_ID -> {
                // Определяем id из URI адреса. Класс ContentUris помогает это сделать
                val id = ContentUris.parseId(uri)
                // Запрос к базе данных для одного элемента
                historyDao.getHistoryCursorById(id)
            }
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
        // Устанавливаем нотификацию при изменении данных в content_uri
        cursor.setNotificationUri(context!!.contentResolver, contentUri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == URI_ALL) { "Wrong URI: $uri" }
        // Добавляем запись о городе
        val entity = convertToWeatherHistoryEntity(values)
        val id: Long = entity.id
        var historyDao = MyApp.getWeatherDbMainThreadMode().weatherDao()
        historyDao.insertHistory(entity)
        val resultUri = ContentUris.withAppendedId(contentUri, id)
        // Уведомляем ContentResolver, что данные по адресу resultUri изменились
        context?.contentResolver?.notifyChange(resultUri, null)
        return resultUri
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var historyDao = MyApp.getWeatherDbMainThreadMode().weatherDao()
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
        // Получаем доступ к данным
        historyDao.updateHistory(convertToWeatherHistoryEntity(values))
        context!!.contentResolver.notifyChange(uri, null)
        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }
        var historyDao = MyApp.getWeatherDbMainThreadMode().weatherDao()

        // Получаем идентификатор записи по адресу
        val id = ContentUris.parseId(uri)
        // Удаляем запись по идентификатору
        historyDao.deleteHistoryById(id)
        // Нотификация на изменение Cursor
        context?.contentResolver?.notifyChange(uri, null)
        return 1
    }
}