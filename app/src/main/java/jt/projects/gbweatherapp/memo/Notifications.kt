package jt.projects.gbweatherapp.memo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import jt.projects.gbweatherapp.MainActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.utils.CHANNEL_HIGH_ID
import jt.projects.gbweatherapp.utils.NOTIFICATION_ID
import jt.projects.gbweatherapp.utils.SHOW_WEATHER_DETAILS_INTENT


fun Context.pushNotification(title: String, text: String, idChannel: String) {
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
        PendingIntent.getActivity(this, 1, notificationIntent, pendingFlags)

    val notification = NotificationCompat.Builder(this, idChannel).apply {
        setContentTitle(title)
        setContentText(text)
        setLights(android.R.color.holo_blue_light, 3000,1000)
        setSmallIcon(android.R.drawable.ic_menu_myplaces)
        setContentIntent(contentIntent)
    }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
}

fun Context.checkChannelReady(idChannel: String): Boolean {
    val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    val channel: NotificationChannel = notificationManager.getNotificationChannel(idChannel)
    return (channel.importance != NotificationManager.IMPORTANCE_NONE)
}

// Если вы помните, при создании уведомления, мы можем в билдере указать приоритет.
// Начиная с Android Oreo приоритеты уведомлений были объявлены устаревшими и заменены параметром канала - важность

fun Context.pushNotificationLocationFound(cityName: String, location: LatLng) {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    val address: Uri = Uri.parse("https://goodmeteo.ru/poisk/?s=${cityName}")
    val intentBrowser = Intent(Intent.ACTION_VIEW, address)
    val pIntentBrowser = PendingIntent.getActivity(this, 2, intentBrowser, pendingFlags)

    val intentDetails = Intent(SHOW_WEATHER_DETAILS_INTENT)
    intentDetails.putExtra("location", location)
    val pIntentDetails = PendingIntent.getBroadcast(
        this,
        0,
        intentDetails,
        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
    )

    val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
        setContentTitle(cityName)
        setContentText("Выберите 1 из вариантов")
        priority = NotificationCompat.PRIORITY_MAX
        setSmallIcon(android.R.drawable.ic_dialog_email)
        setLargeIcon(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.weather
            )
        )
        setAutoCancel(true)
        setTimeoutAfter(5000)
        //setOngoing(true)
        setUsesChronometer(true)
        //setProgress(100, 50, false)
        addAction(R.drawable.ic_baseline_star_24, "Карточка детализации", pIntentDetails)
        addAction(R.drawable.ic_baseline_search_24, "Открыть в браузере..", pIntentBrowser)
        //addAction(R.drawable.ic_baseline_home_24, "Главное меню", contentIntent)
        priority = NotificationCompat.PRIORITY_MAX
    }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
}