package com.ilia_zusik.weatherapp.domain.usecases

import com.ilia_zusik.weatherapp.domain.repositories.WeatherRepository
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    fun weather(cityName: String) = repository.weather(cityName)

}