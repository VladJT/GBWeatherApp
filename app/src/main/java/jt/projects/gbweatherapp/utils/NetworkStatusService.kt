package jt.projects.gbweatherapp.utils

import android.app.IntentService
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager


const val NetworkStatusIntent = "NetworkStatusIntent"
const val NetworkStatusExtra = "NetworkStatusExtra"

class NetworkStatusService(name: String = "NetworkStatusService") : IntentService(name) {

    var networkStatus = ""
    override fun onHandleIntent(intent: Intent?) {
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
        return capabilities?.transportInfo?.toString() ?: "нет сети"
    }

}