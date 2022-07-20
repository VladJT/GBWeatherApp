package jt.projects.gbweatherapp

import android.app.ActivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import jt.projects.gbweatherapp.model.repository.RepositoryRoomImpl
import jt.projects.gbweatherapp.ui.contacts.ContactsFragment
import jt.projects.gbweatherapp.ui.favorites.FavoritesFragment
import jt.projects.gbweatherapp.ui.home.HomeFragment
import jt.projects.gbweatherapp.ui.search.SearchFragment
import jt.projects.gbweatherapp.ui.weatherdetails.WeatherDetailsFragment
import jt.projects.gbweatherapp.utils.showSnackBarShort
import jt.projects.gbweatherapp.viewmodel.SharedPref


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Нам нужно создать фрагмент со списком всего лишь один раз — при первом запуске. Задачу по
        // пересозданию фрагментов после поворота экрана берет на себя FragmentManager.
        if (savedInstanceState == null) {
            showFragment(HomeFragment.newInstance())
        }
        initBottomMenu()
        initAppBarThemeSwitch()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // мы получаем Меню приложения и с помощью специального MenuInflater’а (по
        //аналогии с LayoutInflater’ом) надуваем кнопки в получаемом меню.
        menuInflater.inflate(R.menu.appbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_service_info -> {
                showAllRunningServices()
            }
            R.id.action_contacts -> {
                showFragmentWithBS(ContactsFragment.newInstance())
            }
            R.id.action_settings -> {
                showMsgDialog("Настройки", "...позже..")
            }
            R.id.action_clean_room -> {
                val repRoom = RepositoryRoomImpl()
                repRoom.deleteAll()
                binding.root.showSnackBarShort("Список избранного очищен")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAllRunningServices() {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val rs = am.getRunningServices(50)
        val sb = StringBuilder()
        for (i in rs.indices) {
            sb.append(rs[i].service.shortClassName.plus("\n"))
        }
        showMsgDialog("Список запущенных процессов", sb.toString())
    }


    private fun initAppBarThemeSwitch() {
        binding.switchTheme.apply {
            if (SharedPref.getData().theme == R.style.Theme_DarkTheme) {
                isChecked = true
            }

            setOnClickListener {
                val appTheme = if (isChecked) {
                    R.style.Theme_DarkTheme
                } else {
                    R.style.Theme_LightTheme
                }
                SharedPref.settings.theme = appTheme
                SharedPref.save()
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
            when (visibleFragment) {
                is WeatherDetailsFragment -> {
                    supportFragmentManager.popBackStack()
                }
                is ContactsFragment -> {
                    supportFragmentManager.popBackStack()
                }
                else -> {
                    showExitDialog()
                }
            }
        }
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
}