package jt.projects.gbweatherapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class City(
    val name: String,// название города
    val lat: Double,//lat	Широта (в градусах).	Число
    val lon: Double//lon	Долгота (в градусах).	Число
) : Parcelable