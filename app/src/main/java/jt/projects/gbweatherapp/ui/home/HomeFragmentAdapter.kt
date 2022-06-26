package jt.projects.gbweatherapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.WeatherCondition


internal class HomeFragmentAdapter :  RecyclerView.Adapter<HomeFragmentAdapter.HomeViewHolder>() {

    var weatherData: List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.weather_card_small_info, parent, false) as View)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }


    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            itemView.findViewById<TextView>(R.id.temperatureValueBig).text = weather.weatherData.temperature.toString()+ "\u2103"
            itemView.findViewById<TextView>(R.id.cityName).text = weather.city.name
            itemView.findViewById<TextView>(R.id.conditionValue).text = WeatherCondition.getRusName(weather.weatherData.condition)

            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    weather.city.name,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}