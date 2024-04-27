package com.ilia_zusik.weatherapp.domain.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ilia_zusik.weatherapp.data.models.display.DisplayWeatherModel
import com.ilia_zusik.weatherapp.data.remote.WeatherApi
import com.ilia_zusik.weatherapp.domain.base.BaseRepository
import com.iliazusik.rickmortyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.ilia_zusik.weatherapp.data.models.display.DisplayWeatherHourModel
import java.util.Locale

class WeatherRepository @Inject constructor(private val api: WeatherApi) : BaseRepository() {

    fun weather(): LiveData<Resource<DisplayWeatherModel>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            val response = api.getWeather("Bishkek")
            if (response.isSuccessful && response.body() != null && response.code() in 200..300) {
                with(response.body()!!) {
                    val responseHourly = api.getHourlyWeather(id)
                    val dataHourly = arrayListOf<DisplayWeatherHourModel>()
                    if (responseHourly.isSuccessful && responseHourly.body() != null && responseHourly.code() in 200..300) {
                        with(responseHourly.body()!!) {
                            list.forEach {
                                dataHourly.add(
                                    DisplayWeatherHourModel(
                                        hour = it.dt_txt.substring(11, 16),
                                        rainPercentage = "${(it.pop * 100).toInt()}%",
                                        imageUrl = "https://openweathermap.org/img/wn/" + it.weather[0].icon + "@2x.png"
                                    )
                                )
                            }

                        }
                    } else {
                        emit(Resource.Error("Unsuccessful response: ${responseHourly.code()}"))
                    }
                    emit(
                        Resource.Success(
                            DisplayWeatherModel(
                                cityName = name,
                                cityInitial = name.substring(0, 3).uppercase(Locale.ROOT),
                                temperature = "${temp.temp.toInt()}°",
                                hourly = dataHourly
                            )
                        )
                    )
                }
            } else {
                emit(Resource.Error("Unsuccessful response: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error!"))
        }
    }
}