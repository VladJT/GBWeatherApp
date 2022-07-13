package jt.projects.gbweatherapp.utils

import android.app.IntentService
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager

// Thread.sleep(100) - это жестко
//какие есть выходы?
//1) вызывать функцию getConnectionInfo только в моменты, когда вам нужно знать, что с сетью
//2) использовать CONNECTIVITY_ACTION событие
//3) использовать триггер android.intent.action.AIRPLANE_MODE с урока

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
        } else result = "Нет сети"
        return result
    }
}