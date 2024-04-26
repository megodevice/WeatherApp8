package com.ilia_zusik.weatherapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.ilia_zusik.weatherapp.domain.weather.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    fun getWeather() = repository.fetchWeather()

    fun getHourlyWeather(weatherId: Int) = repository.fetchHourlyWeather(weatherId)

}