package jt.projects.gbweatherapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import jt.projects.gbweatherapp.R

private const val SHARED_PREF_NAME = "database"
private const val THEME_KEY = "theme"

class SharedPref {
    companion object {
        private lateinit var sharedPref: SharedPreferences

        fun initSharedPreferencesContext(context: Context) {
            sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        }

        fun saveAppTheme(theme: Int) {
            val editor: SharedPreferences.Editor = sharedPref.edit().putInt(
                THEME_KEY,
                theme
            )
            editor.apply()
        }

        fun getAppTheme(): Int {
            return sharedPref.getInt(THEME_KEY, R.style.Theme_LightTheme)
        }
    }
}