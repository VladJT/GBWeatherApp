package jt.projects.gbweatherapp.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

//Это абстрактный класс, у которого должна быть определена аннотация @Database со всеми
//табличными объектами. В нашем случае это массив всего с одной таблицей. Версию БД указываем в
//version. Обязательно увеличиваем версию при изменении структуры БД. Параметр exportSchema
//указывает, создавать или нет файл в проекте, где будет храниться история версий БД. Это
//совершенно необязательно, но в коммерческих проектах — практика хорошая. В классе также
//требуется определить метод, возвращающий объект доступа к данным
// arrayOf(WeatherEntity::class, WeatherHistoryEntity::class)
@Database(entities = [WeatherEntity::class, WeatherHistoryEntity::class], version = 3)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDAO
}