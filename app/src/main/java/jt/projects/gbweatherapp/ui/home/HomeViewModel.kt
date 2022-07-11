package jt.projects.gbweatherapp.ui.home

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import jt.projects.gbweatherapp.BuildConfig
import jt.projects.gbweatherapp.model.Repository
import jt.projects.gbweatherapp.model.RepositoryImpl
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.utils.REQUEST_API_KEY
import jt.projects.gbweatherapp.utils.WeatherCondition
import jt.projects.gbweatherapp.viewmodel.AppState
import okhttp3.*
import java.io.IOException

class HomeViewModel : ViewModel() {
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repositoryImpl: Repository = RepositoryImpl()

    fun getData(): LiveData<AppState> = liveDataToObserve

    fun getDataFromInternet(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading

        var cities = if (isRussian)
            repositoryImpl.getWeatherFromLocalStorageRus() else
            repositoryImpl.getWeatherFromLocalStorageWorld()

        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder().apply {  // Создаём строителя запроса
            header(REQUEST_API_KEY, BuildConfig.WEATHER_API_KEY) // Создаём заголовок запроса
        }


        for (i in 0..cities.size - 1) {
            builder.url("https://api.weather.yandex.ru/v2/forecast?lat=${cities[i].city.lat}&lon=${cities[i].city.lon}")
            val request: Request = builder.build() // Создаём запрос
            val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем

            call.enqueue(object : Callback {
                val handler: Handler = Handler()

                // Вызывается, если ответ от сервера пришёл
                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response) {
                    val serverResponce: String? = response.body()?.string()
                    // Синхронизируем поток с потоком UI
                    if (response.isSuccessful && serverResponce != null) {
                        handler.post {
                            val weatherDTO =
                                Gson().fromJson(serverResponce, WeatherDTO::class.java)
                            with(cities[i]) {
                                temperature = weatherDTO.fact.temp
                                feelsLike = weatherDTO.fact.feelsLike
                                condition = WeatherCondition.getRusName(weatherDTO.fact.condition)
                            }
                            liveDataToObserve.postValue(AppState.SuccessMulti(cities))
                        }
                    } else {
                        //     TODO(PROCESS_ERROR)
                    }
                }

                // Вызывается при сбое в процессе запроса на сервер
                override fun onFailure(call: Call?, e: IOException?) {
                    //    TODO(PROCESS_ERROR)
                }
            })
        }
    }

    fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserve.value = AppState.Loading

        var result = if (isRussian)
            repositoryImpl.getWeatherFromLocalStorageRus() else
            repositoryImpl.getWeatherFromLocalStorageWorld()

        Thread {
            when ((0..5).random()) {
                0, 1, 2, 3 -> {
                    Thread.sleep(200)
                    liveDataToObserve.postValue(AppState.SuccessMulti(result))
                }
                4, 5 -> {
                    Thread.sleep(200)
                    liveDataToObserve.postValue(AppState.Error(Throwable("Не удалось получить данные")))
                }
            }
        }.start()
    }

}