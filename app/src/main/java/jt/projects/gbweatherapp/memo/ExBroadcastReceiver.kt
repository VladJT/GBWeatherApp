package jt.projects.gbweatherapp.memo

import android.R
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import jt.projects.gbweatherapp.utils.NOTIFICATION_CHANNEL_ID


// типовой BroadcastReceiver
class ExBroadcastReceiver : BroadcastReceiver() {

    private var messageId = 0

    override fun onReceive(context: Context, intent: Intent) {
        StringBuilder().apply {
            append("СООБЩЕНИЕ ОТ СИСТЕМЫ\n")
            append("Action: ${intent.action}")
            toString().also {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // создать нотификацию
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .setContentTitle("СООБЩЕНИЕ ОТ СИСТЕМЫ")
            .setContentText("${intent.action}")
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(messageId++, notificationBuilder.build())
    }
}