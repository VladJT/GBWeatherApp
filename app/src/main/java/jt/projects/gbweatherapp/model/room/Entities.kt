package jt.projects.gbweatherapp.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "weather_entity_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val lat: Double,
    val lon: Double,
    var temperature: Int,
    var feelsLike: Int,
    var condition: String,
    var icon: String,
    val now: Long
)

@Entity(tableName = "weather_history_table")
data class WeatherHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val lat: Double,
    val lon: Double,
    var temperature: Int,
    val now: Long
)