package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.repository.DetailsRepository
import jt.projects.gbweatherapp.model.repository.DetailsRepositoryImpl
import jt.projects.gbweatherapp.model.repository.RemoteDataSource
import jt.projects.gbweatherapp.viewmodel.DTOAppState
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"


class WeatherDetailsViewModel : ViewModel() {

    private val detailsLiveData: MutableLiveData<DTOAppState> = MutableLiveData()
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(requestLink: String) {
        detailsLiveData.value = DTOAppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(requestLink, callBack)
    }

    private val callBack = object : Callback {
        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    DTOAppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call, e: IOException) {
            detailsLiveData.postValue(
                DTOAppState.Error(
                    Throwable(
                        e?.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }

    private fun checkResponse(serverResponse: String): DTOAppState {
        val weatherDTO: WeatherDTO =
            Gson().fromJson(serverResponse, WeatherDTO::class.java)
        val fact = weatherDTO.fact
        return if (fact == null) {
            DTOAppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            DTOAppState.Success(weatherDTO)
        }
    }

}