package jt.projects.gbweatherapp.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.model.repository.OperationType
import jt.projects.gbweatherapp.ui.OnItemViewClickListener
import jt.projects.gbweatherapp.utils.ICON_URL
import jt.projects.gbweatherapp.utils.WeatherCondition
import jt.projects.gbweatherapp.utils.toDateTime

internal class FavFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<FavFragmentAdapter.HomeViewHolder>() {

    var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.weather_card_small_info, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }


    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(weather: Weather) {
            with(itemView) {
                findViewById<TextView>(R.id.temperatureValueBigSmallCard).text = ""
                //   weather.temperature.toString().toTemperature()

                findViewById<TextView>(R.id.cityNameSmallCard).text = weather.city.name
                findViewById<TextView>(R.id.conditionValueSmallCard).text =
                    WeatherCondition.getRusName(weather.condition)
                findViewById<TextView>(R.id.dateOfWeatherSmallCard).text =
                    weather.now.toDateTime()
                val imageWeather = findViewById<AppCompatImageView>(R.id.weatherIconSmallCard)
                imageWeather.load(String.format(ICON_URL, weather.icon)) {}

                findViewById<ToggleButton>(R.id.favButton).apply {
                    isChecked = true

                    setOnClickListener {
                        if (!isChecked) {
                            onItemViewClickListener?.onButtonFavoritesClick(
                                weather,
                                OperationType.DELETE
                            )
                        } else {
                            onItemViewClickListener?.onButtonFavoritesClick(
                                weather,
                                OperationType.ADD
                            )
                        }
                    }
                }

                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(weather)
                }
            }
        }
    }

}