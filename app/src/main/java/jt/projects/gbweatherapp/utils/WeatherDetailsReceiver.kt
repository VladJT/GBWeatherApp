package jt.projects.gbweatherapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import jt.projects.gbweatherapp.BaseActivity

class WeatherDetailsReceiver : BroadcastReceiver() {
    companion object {
        lateinit var appContext: BaseActivity

        fun setAppContext(context: Context) {
            appContext = context as BaseActivity
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val location = intent.getParcelableExtra<LatLng>("location")
            location?.let {
                appContext.showWeatherDetails(location)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }
}