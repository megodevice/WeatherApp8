package com.ilia_zusik.weatherapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilia_zusik.weatherapp.domain.weather.WeatherRepository
import com.ilia_zusik.weatherapp.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository) :
    ViewModel() {

    private val weather = MutableSharedFlow<String>()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val resource = weather.map { it }
        .flatMapLatest { repository.weather(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Resource.Loading())

    val isLoading = resource.map {
        it.isLoading
    }
    val isFail = resource.map {
        it.isError
    }
    val data = resource.map {
        it.dataOrNull
    }

    fun submit(cityName: String) {
        viewModelScope.launch {
            weather.emit(cityName)
        }
    }
}