package jt.projects.gbweatherapp.model.dto


import com.google.gson.annotations.SerializedName

data class GeoObject(
    @SerializedName("country")
    val country: Country,
    @SerializedName("district")
    val district: District,
    @SerializedName("locality")
    val locality: Locality,
    @SerializedName("province")
    val province: Province
)