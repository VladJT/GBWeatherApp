package jt.projects.gbweatherapp.viewmodel

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import jt.projects.gbweatherapp.R


private const val SHARED_PREF_NAME = "database"
private const val DATA_KEY = "JSON_SETTINGS"

class SharedPref {
    companion object {
        lateinit var settings: Settings
        private lateinit var sharedPref: SharedPreferences

        fun initSharedPreferencesContext(context: Context) {
            sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        }

//        fun saveAppTheme(theme: Int) {
//            val editor: SharedPreferences.Editor = sharedPref.edit().putInt(
//                THEME_KEY,
//                theme
//            )
//            editor.apply()
//        }
//
//        fun getAppTheme(): Int {
//            return sharedPref.getInt(THEME_KEY, R.style.Theme_LightTheme)
//        }

        fun getData(): Settings {
            val jsonNotes = sharedPref.getString(DATA_KEY, null)
            val type = object : TypeToken<Settings>() {}.type
            val data = GsonBuilder().create().fromJson<Any>(jsonNotes, type)
            settings = if (data == null) {
                Settings()
            } else data as Settings
            return settings
        }

        fun save() {
            var jsonNotes: String? = ""
            settings?.let {
                jsonNotes = GsonBuilder().create().toJson(it)
            }
            sharedPref.edit().putString(DATA_KEY, jsonNotes).apply()
        }
    }

    // класс хранения настроек в формате JSON
    data class Settings(
        var theme: Int = R.style.Theme_LightTheme,//тема приложения
        var isDataSetRus: Boolean = true// список городов - ru / world
    )
}