package jt.projects.gbweatherapp.utils

import android.app.IntentService
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager



const val NetworkStatusIntent = "NetworkStatusIntent"
const val NetworkStatusExtra = "NetworkStatusExtra"

class NetworkStatusService(name: String = "NetworkStatusService") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        var networkStatus = ""
        var result = ""
        while (true) {
            result = getConnectionInfo()
            if (!result.equals(networkStatus)) {
                networkStatus = result
                LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(
                        Intent(NetworkStatusIntent).putExtra(
                            NetworkStatusExtra,
                            networkStatus
                        )
                    )
                Thread.sleep(100)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getConnectionInfo(): String {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
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
        }else  result = "Нет сети"
        return result
    }
}