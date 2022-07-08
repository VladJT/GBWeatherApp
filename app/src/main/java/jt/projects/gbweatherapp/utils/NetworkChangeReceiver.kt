package jt.projects.gbweatherapp.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast


class NetworkChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        var connInfo = ""
        val noConnectivity =
            intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
        if (!noConnectivity) {
            connInfo = "Соединение восстановлено"
        } else {
            connInfo = "Соединение потеряно"
        }

        StringBuilder().apply {
            append("Network: ")
            append(connInfo)
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}