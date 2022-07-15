package jt.projects.gbweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    var name: String,// название города
    var lat: Double,//lat	Широта (в градусах).	Число
    var lon: Double//lon	Долгота (в градусах).	Число
) : Parcelable