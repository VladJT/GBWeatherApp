package jt.projects.gbweatherapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.OnItemViewClickListener
import jt.projects.gbweatherapp.utils.WeatherCondition


internal class HomeFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<HomeFragmentAdapter.HomeViewHolder>() {

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
                findViewById<TextView>(R.id.temperatureValueBig).text =
                    weather.fact.temp.toString().plus("\u2103")
                findViewById<TextView>(R.id.cityName).text = weather.city.name
                findViewById<TextView>(R.id.conditionValue).text =
                    WeatherCondition.getRusName(weather.fact.condition)

                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(weather)
//                Toast.makeText(
//                    itemView.context,
//                    weather.city.name,
//                    Toast.LENGTH_LONG
//                ).show()
                }
            }
        }
    }

}