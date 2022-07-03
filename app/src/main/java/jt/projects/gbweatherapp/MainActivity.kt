package jt.projects.gbweatherapp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import jt.projects.gbweatherapp.databinding.ActivityMainBinding
import jt.projects.gbweatherapp.ui.favorites.FavoritesFragment
import jt.projects.gbweatherapp.ui.home.HomeFragment
import jt.projects.gbweatherapp.ui.search.SearchFragment
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.viewmodel.SharedPref

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        initBottomMenu()
        initAppBarThemeSwitch()
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