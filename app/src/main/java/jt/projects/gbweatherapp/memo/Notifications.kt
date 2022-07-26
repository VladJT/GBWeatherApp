package jt.projects.gbweatherapp.memo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.app.NotificationCompat
import com.bumptech.glide.util.Util
import jt.projects.gbweatherapp.MainActivity
import jt.projects.gbweatherapp.utils.CHANNEL_HIGH_ID
import jt.projects.gbweatherapp.utils.NOTIFICATION_ID


fun Context.pushNotification(title: String, text: String) {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val notificationIntent =
        Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

    val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    val contentIntent =
        PendingIntent.getActivity(this, 1, notificationIntent,pendingFlags)

    val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
        setContentTitle(title)
        setContentText(text)
        priority = NotificationCompat.PRIORITY_MAX
        setSmallIcon(android.R.drawable.ic_menu_myplaces)
        setContentIntent(contentIntent)
//        addAction(
//            android.R.drawable.ic_lock_idle_charging, "Click me!",
//            snoozePendingIntent)
        priority = NotificationCompat.PRIORITY_MAX
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelHigh = NotificationChannel(
            CHANNEL_HIGH_ID,
            CHANNEL_HIGH_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
        channelHigh.description = "Канал 1"
        notificationManager.createNotificationChannel(channelHigh)
    }
    notificationManager.notify(NOTIFICATION_ID, notification.build())
}