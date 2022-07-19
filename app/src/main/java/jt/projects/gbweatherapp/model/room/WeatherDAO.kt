package jt.projects.gbweatherapp.model.room

import android.database.Cursor
import androidx.room.*

@Dao
interface WeatherDAO {
    // ** WeatherEntity **
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)

    @Update
    fun update(entity: WeatherEntity)

    @Query("DELETE FROM weather_entity_table")
    fun deleteAll()

    @Query("DELETE FROM weather_entity_table where lat=:lat and lon=:lon")
    fun deleteByLocation(lat: Double, lon: Double)

    @Query("DELETE FROM weather_entity_table WHERE id = :id")
    fun deleteById(id: Long)

    @Query("SELECT * FROM weather_entity_table")
    fun getWeatherAll(): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table where lat=:lat and lon=:lon")
    fun getWeatherByLocation(lat: Double, lon: Double): List<WeatherEntity>


    // ** WeatherHistoryEntity **
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(weatherHistoryEntity: WeatherHistoryEntity)

    @Update
    fun updateHistory(entity: WeatherHistoryEntity)

    @Query("DELETE FROM weather_entity_table WHERE id = :id")
    fun deleteHistoryById(id: Long)

    @Query("SELECT id, lat, lon, temperature FROM weather_entity_table")
    fun getHistoryCursor(): Cursor

    @Query("SELECT id, lat, lon, temperature FROM weather_entity_table WHERE id = :id")
    fun getHistoryCursorById(id: Long): Cursor

    @Query("SELECT * FROM weather_history_table where lat=:lat and lon=:lon order by id asc")
    fun getHistoryByLocation(lat: Double, lon: Double): List<WeatherHistoryEntity>

}