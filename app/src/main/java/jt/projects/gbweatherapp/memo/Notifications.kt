package jt.projects.gbweatherapp.memo

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.google.android.gms.maps.model.LatLng
import jt.projects.gbweatherapp.BaseActivity
import jt.projects.gbweatherapp.MainActivity
import jt.projects.gbweatherapp.R
import jt.projects.gbweatherapp.utils.*
import java.io.IOException

// Если вы помните, при создании уведомления, мы можем в билдере указать приоритет.
// Начиная с Android Oreo приоритеты уведомлений были объявлены устаревшими и заменены параметром канала - важность

class Notifications {
    interface NotificationSettingsCallback {
        fun onGoSettings(idChannel: String)
    }

    companion object {
        lateinit var context: Context
        lateinit var notificationManager: NotificationManager
        lateinit var callback: NotificationSettingsCallback

        fun init(context: Context, callback: NotificationSettingsCallback) {
            this.context = context
            this.callback = callback
            notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            initNotificationChannels()
        }

        // инициализация каналов нотификаций
        private fun initNotificationChannels() {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannelGroup(
                    NotificationChannelGroup(MY_GROUP_ID, MY_GROUP_ID)
                )

                NotificationChannel(
                    CHANNEL_HIGH_ID,
                    CHANNEL_HIGH_ID,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Канал c IMPORTANCE_HIGH (push-уведомления из MAP-фрагмента)"
                    group = MY_GROUP_ID
                }.also { notificationManager.createNotificationChannel(it) }

                NotificationChannel(
                    CHANNEL_LOW_ID,
                    CHANNEL_LOW_ID,
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Канал c IMPORTANCE_LOW (push-уведомления с FCM)"
                    group = MY_GROUP_ID
                }.also { notificationManager.createNotificationChannel(it) }
            }
        }

        private fun tryToNotify(
            idChannel: String,
            idNotification: Int,
            notification: Notification
        ) {
            if (checkChannelReady(idChannel)) {
                notificationManager.notify(idNotification, notification)
            }else{
                callback.onGoSettings(idChannel)
                if (checkChannelReady(idChannel)) {
                    notificationManager.notify(idNotification, notification)
                }
            }
        }

        private fun checkChannelReady(idChannel: String): Boolean {
            val channel = notificationManager.getNotificationChannel(idChannel)
            return (channel.importance != NotificationManager.IMPORTANCE_NONE)
        }

        fun showChannelSettings(idChannel: String) {
            val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, idChannel)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.applicationInfo.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            context.startActivity(intent)
        }

        fun pushNotification(title: String, text: String, idChannel: String) {
            val notificationIntent =
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
            val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            val contentIntent =
                PendingIntent.getActivity(context, 1, notificationIntent, pendingFlags)

            val notification = NotificationCompat.Builder(context, idChannel).apply {
                setContentTitle(title)
                setContentText(text)
                setLights(android.R.color.holo_blue_light, 3000, 1000)
                setSmallIcon(android.R.drawable.ic_menu_myplaces)
                setContentIntent(contentIntent)
            }

            tryToNotify(idChannel, NOTIFICATION_ID, notification.build())
        }

        fun pushNotificationLocationFound(cityName: String, location: LatLng) {
            val idChannel = CHANNEL_HIGH_ID

            val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }

            val address: Uri = Uri.parse("https://goodmeteo.ru/poisk/?s=${cityName}")
            val intentBrowser = Intent(Intent.ACTION_VIEW, address)
            val pIntentBrowser = PendingIntent.getActivity(context, 2, intentBrowser, pendingFlags)

            val intentDetails = Intent(SHOW_WEATHER_DETAILS_INTENT)
            intentDetails.putExtra("location", location)
            val pIntentDetails = PendingIntent.getBroadcast(
                context,
                0,
                intentDetails,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            val notification = NotificationCompat.Builder(context, idChannel).apply {
                setContentTitle(cityName)
                setContentText("Выберите 1 из вариантов")
                priority = NotificationCompat.PRIORITY_MAX
                setSmallIcon(android.R.drawable.ic_dialog_email)
                setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
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
            tryToNotify(idChannel, NOTIFICATION_ID, notification.build())
        }
    }

}