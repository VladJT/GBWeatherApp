package jt.projects.gbweatherapp.model.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fact(
//    @SerializedName("accum_prec")
//    val accumPrec: AccumPrec,
    @SerializedName("cloudness")
    val cloudness: Double = 0.0,
    @SerializedName("condition")
    val condition: String = "",
    @SerializedName("daytime")
    val daytime: String = "",
    @SerializedName("feels_like")
    val feelsLike: Int = 0,
    @SerializedName("humidity")
    val humidity: Int = 0,
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("prec_prob")
    val precProb: Int = 0,
    @SerializedName("prec_strength")
    val precStrength: Double = 0.0,
    @SerializedName("prec_type")
    val precType: Int = 0,
    @SerializedName("pressure_mm")
    val pressureMm: Double = 0.0,
    @SerializedName("pressure_pa")
    val pressurePa: Double = 0.0,
    @SerializedName("temp")
    val temp: Int = 0,
    @SerializedName("uptime")
    val uptime: Int = 0,
    @SerializedName("uv_index")
    val uvIndex: Int = 0,
    @SerializedName("wind_dir")
    val windDir: String = "",
    @SerializedName("wind_gust")
    val windGust: Double = 0.0,
    @SerializedName("wind_speed")
    val windSpeed: Double = 0.0
) : Parcelable