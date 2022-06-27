package jt.projects.gbweatherapp.utils

import jt.projects.gbweatherapp.model.Weather


interface OnItemViewClickListener {
    fun onItemViewClick(weather: Weather)
}