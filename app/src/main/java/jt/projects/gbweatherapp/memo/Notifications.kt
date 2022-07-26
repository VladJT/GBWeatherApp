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
        PendingIntent.getActivity(this, 1, notificationIntent, pendingFlags)

    val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
        setContentTitle(title)
        setContentText(text)
        priority = NotificationCompat.PRIORITY_MAX
        setSmallIcon(android.R.drawable.ic_menu_myplaces)
        setContentIntent(contentIntent)
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


fun Context.pushNotification(cityName: String, location: LatLng) {
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


    val address: Uri = Uri.parse("https://goodmeteo.ru/poisk/?s=${cityName}")
    val intentBrowser = Intent(Intent.ACTION_VIEW, address)
    val pIntentBrowser = PendingIntent.getActivity(this, 2, intentBrowser, pendingFlags)

    val intentDetails = Intent("show_weather_details")
        //intentDetails.putExtra("location", location)
    val pIntentDetails = PendingIntent.getBroadcast(this, 0, intentDetails, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)


    val notification = NotificationCompat.Builder(this, CHANNEL_HIGH_ID).apply {
        setContentTitle(cityName)
        setContentText("Погода в ".plus(cityName))
        priority = NotificationCompat.PRIORITY_MAX
        setSmallIcon(android.R.drawable.ic_dialog_email)
        setContentIntent(contentIntent)
//        setStyle(
//            NotificationCompat
//                .BigTextStyle()
//                .bigText(
//                    "Прогнозирование погоды - это применение науки и техники для прогнозирования состояния атмосферы в данном месте и в определенное время. " +
//                            "Люди пытались неофициально предсказывать погоду на протяжении тысячелетий, а официально - с 19 века. " +
//                            "Прогнозы погоды составляются путем сбора количественных данных о текущем состоянии атмосферы, " +
//                            "суши и океана и использования метеорологии для прогнозирования того, как изменится атмосфера в данном месте."
//                )
//        )
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.k
                )
            )
        setAutoCancel(true); // автоматически закрыть уведомление после нажатия
        setProgress(100, 50, false)
        addAction(R.drawable.ic_baseline_home_24, "Главное меню", contentIntent)
        addAction(android.R.drawable.ic_menu_mapmode, "В браузере", pIntentBrowser)
        addAction(R.drawable.ic_baseline_star_24, "Details", pIntentDetails)
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
