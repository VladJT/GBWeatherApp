package jt.projects.gbweatherapp.model.dto.others


import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("biomet")
    val biomet: Biomet,
    @SerializedName("date")
    val date: String,
    @SerializedName("date_ts")
    val dateTs: Int,
    @SerializedName("hours")
    val hours: List<Hour>,
    @SerializedName("moon_code")
    val moonCode: Int,
    @SerializedName("moon_text")
    val moonText: String,
    @SerializedName("parts")
    val parts: Parts,
    @SerializedName("rise_begin")
    val riseBegin: String,
    @SerializedName("set_end")
    val setEnd: String,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("week")
    val week: Int
)