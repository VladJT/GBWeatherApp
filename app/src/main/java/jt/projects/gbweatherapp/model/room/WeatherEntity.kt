package jt.projects.gbweatherapp.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_entity_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,// название города
    val lat: Double,
    val lon: Double,
    var temperature: Int = 0,
    var feelsLike: Int = 0,
    var condition: String = "--",
    var icon: String = "ovc"
)