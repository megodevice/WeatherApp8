package com.ilia_zusik.weatherapp.domain.models

data class DisplayWeatherModel(
    val date: String,
    val cityName: String,
    val cityInitial: String,
    val temperature: String,
    val hourly: List<DisplayWeatherHourModel>
)

data class DisplayWeatherHourModel(
    val hour: String,
    val rainPercentage: String,
    val imageUrl: String
)
