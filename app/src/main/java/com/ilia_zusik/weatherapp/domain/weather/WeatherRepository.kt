package com.ilia_zusik.weatherapp.domain.weather

import com.ilia_zusik.weatherapp.data.remote.WeatherApi
import com.ilia_zusik.weatherapp.domain.base.BaseRepository
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherApi) : BaseRepository() {

    fun fetchWeather() = doRequest { api.getWeather("Bishkek") }
    fun fetchHourlyWeather(weatherId: Int) = doRequest { api.getHourlyWeather(weatherId) }

}