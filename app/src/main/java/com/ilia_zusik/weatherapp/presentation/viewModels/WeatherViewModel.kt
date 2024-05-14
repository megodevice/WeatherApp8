package com.ilia_zusik.weatherapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilia_zusik.weatherapp.domain.models.DisplayWeatherModel
import com.ilia_zusik.weatherapp.domain.usecases.WeatherUseCase
import com.ilia_zusik.weatherapp.domain.utils.UiResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase
) :
    ViewModel() {

    private val _weather: MutableStateFlow<UiResource<DisplayWeatherModel>> =
        MutableStateFlow(UiResource.Loading())
    val weather: StateFlow<UiResource<DisplayWeatherModel>> = _weather

    fun getWeather(cityName: String) {
        viewModelScope.launch {
            weatherUseCase.weather(cityName).collectLatest { resource ->
                when (resource) {
                    is UiResource.Error -> _weather.emit(UiResource.Error(resource.message!!))
                    is UiResource.Loading -> _weather.emit(UiResource.Loading())
                    is UiResource.Success -> _weather.emit(UiResource.Success(resource.data!!))
                }
            }
        }
    }
}