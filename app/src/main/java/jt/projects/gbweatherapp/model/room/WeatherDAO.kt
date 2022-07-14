package jt.projects.gbweatherapp.model.room

import androidx.room.*

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)

    @Query(
        "Insert into weather_entity_table(name,lat,lon,temperature,feelsLike,condition,icon) " +
                "values (:name,:lat,:lon,:temperature,:feelsLike,:condition,:icon)"
    )
    fun insertNative(
        name: String,
        lat: Double,
        lon: Double,
        temperature: Int,
        feelsLike: Int,
        condition: String,
        icon: String
    )


    @Query("SELECT * FROM weather_entity_table")
    fun getWeatherAll(): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table where lat=:lat and lon=:lon")
    fun getWeatherByLocation(lat: Double, lon: Double): List<WeatherEntity>

//    @Query("SELECT * FROM HistoryEntity")
//    fun all(): List<HistoryEntity>
//    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
//    fun getDataByWord(city: String): List<HistoryEntity>
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: WeatherEntity)

    @Delete
    fun delete(entity: WeatherEntity)
}