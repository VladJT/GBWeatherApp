package jt.projects.gbweatherapp

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import jt.projects.gbweatherapp.model.room.WeatherDatabase
import jt.projects.gbweatherapp.utils.ROOM_DB_NAME_WEATHER

//спользование класса Application для создания общедоступных объектов — это нормальная
//практика. В нём создаётся не только БД, но и классы для логирования событий, аналитики, внедрения
//зависимостей и т. д. Это те классы, которые надо создавать единожды, но использовать везде

// !! обязательно добавляем в манифест android:name=".MyApp"
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: MyApp? = null
        fun getApp() = appInstance!!

        private var dbInAsyncMode: WeatherDatabase? = null
        private var dbInUiThread: WeatherDatabase? = null

        // для работы с Room в асинхронном режиме
        fun getWeatherDbAsyncMode(): WeatherDatabase {
            if (dbInAsyncMode == null) {
                dbInAsyncMode = Room.databaseBuilder(
                    getApp().applicationContext,
                    WeatherDatabase::class.java,
                    ROOM_DB_NAME_WEATHER
                )
                    //.fallbackToDestructiveMigration()
                    // .addMigrations(MIGRATION_1_3)
                    .build()
            }
            return dbInAsyncMode!!
        }

        // для работы с Room в главном потоке
        fun getWeatherDbMainThreadMode(): WeatherDatabase {
            if (dbInUiThread == null) {
                dbInUiThread = Room.databaseBuilder(
                    getApp().applicationContext,
                    WeatherDatabase::class.java,
                    ROOM_DB_NAME_WEATHER
                )
                    .allowMainThreadQueries()// - возможность запросов в главном потоке
                    //.fallbackToDestructiveMigration()
                    // .addMigrations(MIGRATION_1_3)
                    .build()
            }
            return dbInUiThread!!
        }

        // образец миграции
        val MIGRATION_1_3 = object : Migration(1, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE weather_entity_table ADD COLUMN icon_new INTEGER NOT NULL DEFAULT ${R.drawable.ic_russia}")
            }
        }
    }
}