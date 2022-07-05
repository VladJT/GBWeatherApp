package jt.projects.gbweatherapp.model.dto


import com.google.gson.annotations.SerializedName

data class Biomet(
    @SerializedName("condition")
    val condition: String,
    @SerializedName("index")
    val index: Int
)