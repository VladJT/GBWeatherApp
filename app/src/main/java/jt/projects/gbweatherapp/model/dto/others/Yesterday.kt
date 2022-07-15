package jt.projects.gbweatherapp.model.dto.others


import com.google.gson.annotations.SerializedName

data class Yesterday(
    @SerializedName("temp")
    val temp: Int
)