package jt.projects.gbweatherapp.memo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.core.app.NotificationCompat


fun Context.pushNotification(title: String, text: String) {
    val CHANNEL_HIGH_ID = "channel_high"
    val CHANNEL_LOW_ID = "channel_low"
    val NOTIFICATION_ID = 1
    val NOTIFICATION_ID2 = 2

    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
        setContentTitle(title)
        setContentText(text)
        //    intent = PendingIntent(Intent)
        setSmallIcon(android.R.drawable.ic_menu_myplaces)
        priority = NotificationCompat.PRIORITY_MAX
    }.build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelHigh = NotificationChannel(
            CHANNEL_HIGH_ID,
            CHANNEL_HIGH_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
        channelHigh.description = "Канал 1"
        notificationManager.createNotificationChannel(channelHigh)
    }
    notificationManager.notify(NOTIFICATION_ID, notification)

//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channelLow =
//            NotificationChannel(CHANNEL_LOW_ID, CHANNEL_LOW_ID, NotificationManager.IMPORTANCE_LOW)
//        channelLow.description = "Канал 2"
//        notificationManager.createNotificationChannel(channelLow)
//    }
//    notificationManager.notify(NOTIFICATION_ID2, notification)
}