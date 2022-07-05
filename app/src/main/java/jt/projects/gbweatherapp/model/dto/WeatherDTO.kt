package jt.projects.gbweatherapp.model.dto


import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: Fact,
    @SerializedName("forecasts")
    val forecasts: List<Forecast>,
    @SerializedName("geo_object")
    val geoObject: GeoObject,
    @SerializedName("info")
    val info: Info,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String,
    @SerializedName("yesterday")
    val yesterday: Yesterday
)