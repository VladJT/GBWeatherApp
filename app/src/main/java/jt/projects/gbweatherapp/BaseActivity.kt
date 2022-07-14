package jt.projects.gbweatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import jt.projects.gbweatherapp.databinding.ActivityMainBinding
import jt.projects.gbweatherapp.utils.NOTIFICATION_CHANNEL_ID
import jt.projects.gbweatherapp.utils.NOTIFICATION_CHANNEL_NAME
import jt.projects.gbweatherapp.utils.NetworkChangeReceiver
import jt.projects.gbweatherapp.utils.old.NetworkStatusExtra
import jt.projects.gbweatherapp.utils.showSnackBarShort
import jt.projects.gbweatherapp.viewmodel.SharedPref

open class BaseActivity : AppCompatActivity() {
    private val networkChangeReceiver = NetworkChangeReceiver()

    lateinit var binding: ActivityMainBinding

    private val networkChangeReceiverAlternative: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            binding.root.showSnackBarShort("${intent.getStringExtra(NetworkStatusExtra)}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(
            networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        applicationContext?.let {
            // ресивер для отслеживания статуса сети
//            LocalBroadcastManager.getInstance(it)
//                .registerReceiver(
//                    networkChangeReceiverAlternative,
//                    IntentFilter(NetworkStatusIntent)
//                )

            // сервис для рассылки сообщений об изменении состояния сети
            //    startService(Intent(it, NetworkStatusService::class.java))
        }


        SharedPref.initSharedPreferencesContext(applicationContext)
        setTheme(SharedPref.getData().theme)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Для канала и нотификации через push-уведомления
        initNotificationChannel()

        // для возможности загрузки изображений SVG & GIF через библиотеку COIL
        val imageLoader = ImageLoader.Builder(applicationContext)
            .components {
                //GIF
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
                //SVG
                add(SvgDecoder.Factory())
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }

    override fun onDestroy() {
        unregisterReceiver(networkChangeReceiver)
//        applicationContext?.let {
//            LocalBroadcastManager.getInstance(it)
//                .unregisterReceiver(networkChangeReceiverAlternative)
//        }
        super.onDestroy()
    }

    // инициализация канала нотификаций
    private fun initNotificationChannel() {
        if (SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}