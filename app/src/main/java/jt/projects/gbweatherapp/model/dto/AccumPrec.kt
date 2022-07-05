package jt.projects.gbweatherapp.model.dto


import com.google.gson.annotations.SerializedName

data class AccumPrec(
    @SerializedName("1")
    val x1: Double,
    @SerializedName("3")
    val x3: Double,
    @SerializedName("7")
    val x7: Double
)