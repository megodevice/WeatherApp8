package com.ilia_zusik.weatherapp.data.remote

import com.ilia_zusik.weatherapp.data.models.current.WeatherModel
import com.ilia_zusik.weatherapp.data.models.hours.HoursWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(

        @Query("q")
        cityName: String,

        @Query("units")
        units: String = "metric",

        ): Response<WeatherModel>

    @GET("forecast")
    suspend fun getHourlyWeather(

        @Query("id")
        weatherId: Int,

    ): Response<HoursWeatherModel>
}