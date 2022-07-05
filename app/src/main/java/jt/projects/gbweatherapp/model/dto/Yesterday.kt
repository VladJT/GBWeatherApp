package jt.projects.gbweatherapp.model.dto


import com.google.gson.annotations.SerializedName

data class Yesterday(
    @SerializedName("temp")
    val temp: Int
)