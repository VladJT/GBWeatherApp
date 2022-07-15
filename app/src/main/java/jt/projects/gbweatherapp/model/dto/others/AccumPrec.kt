package jt.projects.gbweatherapp.model.dto.others


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccumPrec(
    @SerializedName("1")
    val x1: Double,
    @SerializedName("3")
    val x3: Double,
    @SerializedName("7")
    val x7: Double
) : Parcelable