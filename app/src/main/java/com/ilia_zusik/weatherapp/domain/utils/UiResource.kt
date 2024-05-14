package com.ilia_zusik.weatherapp.domain.utils

sealed class UiResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T>: UiResource<T>()

    class Success<T>(data: T) : UiResource<T>(data = data)

    class Error<T>(message: String) : UiResource<T>(message = message)

}