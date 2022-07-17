package jt.projects.gbweatherapp.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import jt.projects.gbweatherapp.MyApp
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.*

internal class FavFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<FavFragmentAdapter.HomeViewHolder>() {

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }

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
                findViewById<TextView>(R.id.temperatureValueBigSmallCard).text =
                    weather.temperature.toString().toTemperature()
                findViewById<TextView>(R.id.cityNameSmallCard).text = weather.city.name
                findViewById<TextView>(R.id.conditionValueSmallCard).text =
                    WeatherCondition.getRusName(weather.condition)
                findViewById<TextView>(R.id.dateOfWeatherSmallCard).text =
                    weather.now.toDateTime()
                val imageWeather = findViewById<AppCompatImageView>(R.id.weatherIconSmallCard)
                imageWeather.load(String.format(ICON_URL, weather.icon)) {}

                val favButton = findViewById<ToggleButton>(R.id.favButton)
                val cityList = MyApp.getWeatherDbMainThreadMode().weatherDao()
                    .getWeatherByLocation(weather.city.lat, weather.city.lon)

                favButton.isChecked = cityList.isNotEmpty()

                favButton.setOnClickListener {
                    if (!favButton.isChecked) {
                        MyApp.getWeatherDbMainThreadMode().weatherDao()
                            .deleteByLocation(weather.city.lat, weather.city.lon)
                        showSnackBarShort(weather.city.name.plus(" удален из Избранного"))
                    } else {
                        MyApp.getWeatherDbMainThreadMode().weatherDao()
                            .insert(convertWeatherToEntity(weather))
                        showSnackBarShort(weather.city.name.plus(" добавлен в Избранное"))
                    }
                }

                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(weather)
                }
            }
        }
    }

}