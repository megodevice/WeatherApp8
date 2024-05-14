package com.ilia_zusik.weatherapp.domain.repositories

import com.ilia_zusik.weatherapp.domain.models.DisplayWeatherModel
import com.ilia_zusik.weatherapp.domain.utils.UiResource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun weather(cityName: String): Flow<UiResource<DisplayWeatherModel>>

}