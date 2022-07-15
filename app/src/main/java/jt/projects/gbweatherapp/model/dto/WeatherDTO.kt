package jt.projects.gbweatherapp.model.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    val fact: Fact,
//    @SerializedName("forecasts")
//    val forecasts: List<Forecast>,
//    @SerializedName("geo_object")
//    val geoObject: GeoObject,
//    @SerializedName("info")
//    val info: Info,
    @SerializedName("now")
    val now: Long = 0,
    @SerializedName("now_dt")
    val nowDt: String = ""
//    @SerializedName("yesterday")
//    val yesterday: Yesterday
) : Parcelable