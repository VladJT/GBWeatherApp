package jt.projects.gbweatherapp

import android.app.Application
import androidx.room.Room
import jt.projects.gbweatherapp.model.room.WeatherDatabase
import jt.projects.gbweatherapp.utils.ROOM_DB_NAME_WEATHER

//спользование класса Application для создания общедоступных объектов — это нормальная
//практика. В нём создаётся не только БД, но и классы для логирования событий, аналитики, внедрения
//зависимостей и т. д. Это те классы, которые надо создавать единожды, но использовать везде

// !! обязательно добавляем в манифест
// android:name=".MyApp"
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: MyApp? = null
        fun getApp() = appInstance!!

        private var db: WeatherDatabase? = null

        fun getWeatherDatabase(): WeatherDatabase {
            if (db == null) {
                db = Room.databaseBuilder(
                    getApp().applicationContext,
                    WeatherDatabase::class.java,
                    ROOM_DB_NAME_WEATHER
                )
                    .allowMainThreadQueries()// - возможность запросов в главном потоке
                    .build()
            }
            return db!!
        }
    }
}