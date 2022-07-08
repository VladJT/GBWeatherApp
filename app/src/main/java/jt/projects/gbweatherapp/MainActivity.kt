package jt.projects.gbweatherapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import jt.projects.gbweatherapp.databinding.ActivityMainBinding
import jt.projects.gbweatherapp.memo.ExBroadcastReceiver
import jt.projects.gbweatherapp.ui.favorites.FavoritesFragment
import jt.projects.gbweatherapp.ui.home.HomeFragment
import jt.projects.gbweatherapp.ui.search.SearchFragment
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.viewmodel.SharedPref


class MainActivity : AppCompatActivity() {

    private val receiver = ExBroadcastReceiver()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver(receiver, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED))

        SharedPref.initSharedPreferencesContext(applicationContext)
        setTheme(SharedPref.getAppTheme())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Нам нужно создать фрагмент со списком всего лишь один раз — при первом запуске. Задачу по
        // пересозданию фрагментов после поворота экрана берет на себя FragmentManager.
        if (savedInstanceState == null) {
            showFragment(HomeFragment.newInstance())
        }

        // Для формирования канала и нотификации инициализируем его в активити
        initNotificationChannel()

        initBottomMenu()
        initAppBarThemeSwitch()
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    // инициализация канала нотификаций
    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "2", "name",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun initAppBarThemeSwitch() {
        binding.switchTheme.apply {
            if (SharedPref.getAppTheme() == R.style.Theme_DarkTheme) {
                isChecked = true
            }

            setOnClickListener {
                val appTheme = if (isChecked) {
                    R.style.Theme_DarkTheme
                } else {
                    R.style.Theme_LightTheme
                }
                SharedPref.saveAppTheme(appTheme)
                recreate()
            }
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments[0]?.let {
            var visibleFragment = it
            for (f in supportFragmentManager.fragments) {
                if (f.isVisible) visibleFragment = f
            }
            if (visibleFragment is WeatherDetailsFragment) {
                supportFragmentManager.popBackStack()
            } else {
                showExitDialog()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun showExitDialog() {
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

    private fun initBottomMenu() {
        binding.buttonHome.setOnClickListener {
            showFragment(HomeFragment.newInstance())
        }

        binding.buttonSearch.setOnClickListener {
            showFragment(SearchFragment.newInstance())
        }

        binding.buttonFavorites.setOnClickListener {
            showFragment(FavoritesFragment.newInstance())
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}