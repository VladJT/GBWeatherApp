package jt.projects.gbweatherapp.memo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import jt.projects.gbweatherapp.MainActivity
import jt.projects.gbweatherapp.utils.CHANNEL_HIGH_ID
import jt.projects.gbweatherapp.utils.NOTIFICATION_ID
import jt.projects.gbweatherapp.utils.NetworkChangeReceiver


fun Context.pushNotification(title: String, text: String) {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val notificationIntent =
        Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    val contentIntent =
        PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val actionIntent  = Intent(this, NetworkChangeReceiver::class.java).apply {
    }
    val snoozePendingIntent: PendingIntent =
        PendingIntent.getBroadcast(this, 0, actionIntent, 0)

    val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
        setContentTitle(title)
        setContentText(text)
        setContentIntent(contentIntent)
        setSmallIcon(android.R.drawable.ic_menu_myplaces)
        addAction(
            android.R.drawable.ic_lock_idle_charging, "Click me!",
            snoozePendingIntent)
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