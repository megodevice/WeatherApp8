package com.ilia_zusik.weatherapp.data.models.hours

import com.google.gson.annotations.SerializedName

data class WeatherHourModel(
    val clouds: Clouds,
    val dt_txt: String,
    @SerializedName("dt")
    val dateUnix: Long,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)