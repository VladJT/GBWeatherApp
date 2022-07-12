package jt.projects.gbweatherapp.ui.weatherdetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import jt.projects.gbweatherapp.model.dto.WeatherDTO
import jt.projects.gbweatherapp.model.repository.DetailsRepository
import jt.projects.gbweatherapp.model.repository.DetailsRepositoryImpl
import jt.projects.gbweatherapp.model.repository.RemoteDataSource
import jt.projects.gbweatherapp.viewmodel.DTOAppState
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"


class WeatherDetailsViewModel : ViewModel() {

    val detailsLiveData: MutableLiveData<DTOAppState> = MutableLiveData()
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSourceOkHttp(requestLink: String) {
        detailsLiveData.value = DTOAppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServerOkHttp(requestLink, callBackOkHttp)
    }

    fun getWeatherFromRemoteSourceRetrofit(lat: Double, lon: Double) {
        detailsLiveData.value = DTOAppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServerRetrofit(lat, lon, callBackRetrofit)
    }

    @Deprecated("OkHTTP")
    private val callBackOkHttp = object : okhttp3.Callback {
        @Throws(IOException::class)
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            val serverResponse: String? = response.body()?.string()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    DTOAppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: okhttp3.Call, e: IOException) {
            detailsLiveData.postValue(
                DTOAppState.Error(
                    Throwable(
                        e?.message ?: REQUEST_ERROR
                    )
                )
            )
        }
    }

    private val callBackRetrofit = object : retrofit2.Callback<WeatherDTO> {
        override fun onResponse(
            call: retrofit2.Call<WeatherDTO>, response:
            retrofit2.Response<WeatherDTO>
        ) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    DTOAppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: retrofit2.Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(
                DTOAppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
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

    private fun checkResponse(serverResponse: WeatherDTO): DTOAppState {
        return if (serverResponse.fact == null) {
            DTOAppState.Error(Throwable(CORRUPTED_DATA))
        } else {
            DTOAppState.Success(serverResponse)
        }
    }
}