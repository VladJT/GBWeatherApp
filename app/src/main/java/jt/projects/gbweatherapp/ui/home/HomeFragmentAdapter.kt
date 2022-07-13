package jt.projects.gbweatherapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.utils.ICON_URL

internal class HomeFragmentAdapter(private var onItemViewClickListener: OnItemViewClickListener?) :
    RecyclerView.Adapter<HomeFragmentAdapter.HomeViewHolder>() {

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
                    weather.temperature.toString().plus("\u2103")
                findViewById<TextView>(R.id.cityNameSmallCard).text = weather.city.name
                findViewById<TextView>(R.id.conditionValueSmallCard).text = weather.condition

                //val imageUrl =
                //    "https://thumbs.dreamstime.com/b/%D0%B4%D0%B8%D0%B7%D0%B0%D0%B9%D0%BD-%D0%BE%D0%B1-%D0%B0%D0%BA%D0%B0-%D0%B8-%D0%BE%D0%B6-%D1%8F-%D1%81%D0%BE-%D0%BD%D1%86%D1%8F-80915204.jpg"
                val imageWeather = findViewById<AppCompatImageView>(R.id.weatherIconSmallCard)

                // GLIDE
                // Glide.with(this).load(imageUrl).into(imageWeather)

                // PICASSO
                //Picasso.get().load(imageUrl).transform(CircleTransformation()).into(imageWeather)

                // Coil
                //imageWeather.load(imageUrl)

                // Picasso.get().load(iconWeatherUrl).into(imageWeather)

                imageWeather.load(String.format(ICON_URL, weather.icon)) {}

                setOnClickListener {
                    onItemViewClickListener?.onItemViewClick(weather)
                }
            }
        }
    }

}