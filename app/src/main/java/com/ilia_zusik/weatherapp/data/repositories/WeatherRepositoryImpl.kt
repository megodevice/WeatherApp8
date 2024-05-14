package com.ilia_zusik.weatherapp.data.repositories

import com.ilia_zusik.weatherapp.data.models.current.WeatherModel
import com.ilia_zusik.weatherapp.data.models.hours.WeatherHourModel
import com.ilia_zusik.weatherapp.data.models.room.RoomWeather
import com.ilia_zusik.weatherapp.data.models.room.RoomWeatherHour
import com.ilia_zusik.weatherapp.data.remote.WeatherApi
import com.ilia_zusik.weatherapp.data.room.WeatherDao
import com.ilia_zusik.weatherapp.data.utils.DataUtils
import com.ilia_zusik.weatherapp.domain.models.DisplayWeatherHourModel
import com.ilia_zusik.weatherapp.domain.models.DisplayWeatherModel
import com.ilia_zusik.weatherapp.domain.repositories.WeatherRepository
import com.ilia_zusik.weatherapp.domain.utils.UiResource
import com.ilia_zusik.weatherapp.domain.utils.toDisplayWeatherHourModel
import com.ilia_zusik.weatherapp.domain.utils.toDisplayWeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : WeatherRepository {
    override fun weather(cityName: String): Flow<UiResource<DisplayWeatherModel>> = flow {
        emit(UiResource.Loading())
        try {
            loadWeather { emit(it) }
            // Здесь нужна проверка если прошло мало времени не делать запрос


            val response = api.getWeather(cityName)
            if (response.isSuccessful && response.body() != null && response.code() in 200..300) {
                with(response.body()!!) {
                    val responseHourly = api.getHourlyWeather(id)
                    val dataHourly = arrayListOf<DisplayWeatherHourModel>()
                    if (responseHourly.isSuccessful && responseHourly.body() != null && responseHourly.code() in 200..300) {
                        responseHourly.body()!!.list.forEach { dataHourly.add(it.toDisplayWeatherHourModel()) }
                    } else {
                        emit(UiResource.Error("Unsuccessful response: ${responseHourly.code()}"))
                    }

                    val result = this.toDisplayWeatherModel(dataHourly)

                    saveWeather(this, responseHourly.body()?.list)

                    emit(UiResource.Success(result))
                }
            } else {
                emit(UiResource.Error("Unsuccessful response: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(UiResource.Error(e.localizedMessage ?: "Unknown error!"))
        }
    }

    private suspend fun loadWeather(emit: suspend (UiResource<DisplayWeatherModel>) -> Unit) {
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
                UiResource.Success(
                    DisplayWeatherModel(
                        date = DataUtils.convertDate(roomWeather.dateUnix),
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
                cityInitial = DataUtils.getCityInitial(weatherModel.name),
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
                        rainPercentage = DataUtils.getRainPercentage(it.pop),
                        imageUrl = DataUtils.getImageUrl(it.weather[0].icon),
                        dateUnix = it.dateUnix * 1000 + weatherModel.timeZoneOffset
                    )
                )
            }
            dao.saveHours(roomHours)
        }
    }

}