package jt.projects.gbweatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import jt.projects.gbweatherapp.databinding.ActivityMainBinding
import jt.projects.gbweatherapp.utils.*
import jt.projects.gbweatherapp.viewmodel.SharedPref

open class BaseActivity : AppCompatActivity() {
    //  private val networkChangeReceiver = NetworkChangeReceiver()

    lateinit var binding: ActivityMainBinding

    private val networkChangeReceiverAlternative: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            binding.root.showSnackBarShort("${intent.getStringExtra(NetworkStatusExtra)}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //    registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        applicationContext?.let {
            // ресивер для отслеживания статуса сети
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(
                    networkChangeReceiverAlternative,
                    IntentFilter(NetworkStatusIntent)
                )

            // сервис для рассылки сообщений об изменении состояния сети
            startService(Intent(it, NetworkStatusService::class.java))
        }


        SharedPref.initSharedPreferencesContext(applicationContext)
        setTheme(SharedPref.getData().theme)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Для канала и нотификации через push-уведомления
        initNotificationChannel()
    }

    override fun onDestroy() {
        //   unregisterReceiver(networkChangeReceiver)
        applicationContext?.let {
            LocalBroadcastManager.getInstance(it)
                .unregisterReceiver(networkChangeReceiverAlternative)
        }
        super.onDestroy()
    }

    // инициализация канала нотификаций
    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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