package jt.projects.gbweatherapp.model.room

import androidx.room.*

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(weatherHistoryEntity: WeatherHistoryEntity)

//    @Query(
//        "Insert into weather_entity_table(name,lat,lon,temperature,feelsLike,condition,icon) " +
//                "values (:name,:lat,:lon,:temperature,:feelsLike,:condition,:icon)"
//    )
//    fun insertNative(
//        name: String,
//        lat: Double,
//        lon: Double,
//        temperature: Int,
//        feelsLike: Int,
//        condition: String, icon: String
//    )


    @Query("SELECT * FROM weather_entity_table")
    fun getWeatherAll(): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table where lat=:lat and lon=:lon")
    fun getWeatherByLocation(lat: Double, lon: Double): List<WeatherEntity>

    @Query("SELECT * FROM weather_history_table where lat=:lat and lon=:lon order by id asc")
    fun getHistoryByLocation(lat: Double, lon: Double): List<WeatherHistoryEntity>

    @Update
    fun update(entity: WeatherEntity)

    @Query("DELETE FROM weather_entity_table")
    fun deleteAll()

    @Query("DELETE FROM weather_entity_table where lat=:lat and lon=:lon")
    fun deleteByLocation(lat: Double, lon: Double)
}