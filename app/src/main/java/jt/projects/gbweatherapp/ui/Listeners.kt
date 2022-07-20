package jt.projects.gbweatherapp.ui

import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.OperationType

interface OnItemViewClickListener {
    fun onItemViewClick(weather: Weather)
    fun onButtonFavoritesClick(weather: Weather, operation: OperationType)
}