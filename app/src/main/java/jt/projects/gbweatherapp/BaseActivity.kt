package jt.projects.gbweatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Geocoder
import android.net.ConnectivityManager
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import com.google.android.gms.maps.model.LatLng
import jt.projects.gbweatherapp.databinding.ActivityMainBinding
import jt.projects.gbweatherapp.model.City
import jt.projects.gbweatherapp.model.Weather
import jt.projects.gbweatherapp.ui.weatherdetails.BUNDLE_EXTRA
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.utils.*
import jt.projects.gbweatherapp.utils.old.NetworkStatusExtra
import jt.projects.gbweatherapp.viewmodel.SharedPref
import java.util.*

open class BaseActivity : PermissionActivity() {
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
//        applicationContext?.let {
            // ресивер для отслеживания статуса сети
//            LocalBroadcastManager.getInstance(it)
//                .registerReceiver(
//                    networkChangeReceiverAlternative,
//                    IntentFilter(NetworkStatusIntent)
//                )

            // сервис для рассылки сообщений об изменении состояния сети
            //    startService(Intent(it, NetworkStatusService::class.java))
//        }


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

    fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }

    fun showFragmentWithBS(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .addToBackStack("")
            .commit()
    }

    fun showWeatherDetails(weather: Weather) {
        supportFragmentManager?.also { manager ->
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_EXTRA, weather)
            manager.beginTransaction()
                .add(R.id.fragment_container, WeatherDetailsFragment.newInstance(bundle))
                .addToBackStack("").commit()
        }
    }

    fun showWeatherDetails(location: LatLng) {
        val geocoder = Geocoder(this, Locale("ru_RU"))
        val city = geocoder.getFromLocation(location.latitude, location.longitude, 10)
        var cityName = "no city found"
        city?.let {
            if (city.size > 0 && city[0].locality != null) {
                cityName = city[0].locality
            }
        }
        showWeatherDetails(Weather(City(cityName, location.latitude, location.longitude)))
    }

    @Suppress("DEPRECATION")
    fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Выход")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton(
                android.R.string.yes
            ) { _, _ -> finish() } // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    @Suppress("DEPRECATION")
    public fun showMsgDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, null)
            .setIcon(android.R.drawable.ic_menu_help)
            .show()
    }

    override fun onDestroy() {
        unregisterReceiver(networkChangeReceiver)
//        applicationContext?.let {
//            LocalBroadcastManager.getInstance(it)
//                .unregisterReceiver(networkChangeReceiverAlternative)
//        }
        super.onDestroy()
    }
}