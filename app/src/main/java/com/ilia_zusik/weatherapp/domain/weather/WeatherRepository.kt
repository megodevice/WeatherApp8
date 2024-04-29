package com.ilia_zusik.weatherapp.domain.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataScope
import androidx.lifecycle.liveData
import com.ilia_zusik.weatherapp.data.models.current.WeatherModel
import com.ilia_zusik.weatherapp.data.models.display.DisplayWeatherModel
import com.ilia_zusik.weatherapp.data.remote.WeatherApi
import com.ilia_zusik.weatherapp.domain.base.BaseRepository
import com.iliazusik.rickmortyapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import com.ilia_zusik.weatherapp.data.models.display.DisplayWeatherHourModel
import com.ilia_zusik.weatherapp.data.models.hours.WeatherHourModel
import com.ilia_zusik.weatherapp.data.models.room.RoomWeather
import com.ilia_zusik.weatherapp.data.models.room.RoomWeatherHour
import com.ilia_zusik.weatherapp.data.room.WeatherDao
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherRepository @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : BaseRepository() {

    fun weather(): LiveData<Resource<DisplayWeatherModel>> = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            loadWeather(this)
            // Здесь нужна проверка если прошло мало времени не делать запрос


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
                                        rainPercentage = getRainPercentage(it.pop),
                                        imageUrl = getImageUrl(it.weather[0].icon)
                                    )
                                )
                            }

                        }
                    } else {
                        emit(Resource.Error("Unsuccessful response: ${responseHourly.code()}"))
                    }

                    val result = DisplayWeatherModel(
                        cityName = name,
                        cityInitial = getCityInitial(name),
                        temperature = "${temp.temp.toInt()}°",
                        hourly = dataHourly,
                        date = convertDate(dateUnix * 1000 + timeZoneOffset)
                    )

                    saveWeather(this, responseHourly.body()?.list)


                    emit(Resource.Success(result))
                }
            } else {
                emit(Resource.Error("Unsuccessful response: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown error!"))
        }
    }

    private suspend fun loadWeather(scope: LiveDataScope<Resource<DisplayWeatherModel>>) =
        with(scope) {
            val weather = dao.getWeather()
            val hourly = dao.getHourly()
            val hours = ArrayList<DisplayWeatherHourModel>()
            weather?.let { roomWeather ->
                hourly?.let { roomHourlyList ->
                    roomHourlyList.forEach {
                        hours.add(
                            DisplayWeatherHourModel(
                                hour = it.hour,
                                rainPercentage = it.rainPercentage,
                                imageUrl = it.imageUrl
                            )
                        )
                    }
                }
                emit(
                    Resource.Success(
                        DisplayWeatherModel(
                            date = convertDate(roomWeather.dateUnix),
                            cityName = roomWeather.cityName,
                            cityInitial = roomWeather.cityInitial,
                            temperature = roomWeather.temperature,
                            hourly = hours
                        )
                    )
                )
            }
        }

    private suspend fun saveWeather(weatherModel: WeatherModel, hours: List<WeatherHourModel>?) {
        dao.clearWeather()
        dao.clearHours()
        dao.saveWeather(
            RoomWeather(
                cityName = weatherModel.name,
                cityInitial = getCityInitial(weatherModel.name),
                temperature = "${weatherModel.temp.temp.toInt()}°",
                dateUnix = weatherModel.dateUnix * 1000 + weatherModel.timeZoneOffset
            )
        )
        hours?.let { hourList ->
            val roomHours = ArrayList<RoomWeatherHour>()
            hourList.forEach {
                roomHours.add(
                    RoomWeatherHour(
                        hour = it.dt_txt.substring(11, 16),
                        rainPercentage = getRainPercentage(it.pop),
                        imageUrl = getImageUrl(it.weather[0].icon),
                        dateUnix = it.dateUnix * 1000 + weatherModel.timeZoneOffset
                    )
                )
            }
            dao.saveHours(roomHours)
        }
    }

    private fun convertDate(millis: Long) =
        SimpleDateFormat("MM.dd - HH:mm", Locale.ROOT).format(Date(millis))

    private fun getCityInitial(cityName: String) =
        cityName.substring(0, 3).uppercase(Locale.ROOT)

    private fun getRainPercentage(pop: Double) = "${(pop * 100).toInt()}%"

    private fun getImageUrl(imageId: String) = "https://openweathermap.org/img/wn/$imageId@2x.png"

}