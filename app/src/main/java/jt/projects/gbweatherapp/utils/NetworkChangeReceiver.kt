package jt.projects.gbweatherapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi


//    registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        var connInfo = getConnectionInfo(context!!)
        Toast.makeText(context, connInfo, Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getConnectionInfo(context: Context): String {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        var result = ""
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                result = "Сеть: мобильный интернет"
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                result = "Сеть: WIFI"
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                result = "Сеть: ETHERNET"
            }
        } else result = "Нет сети"
        return result
    }

}
