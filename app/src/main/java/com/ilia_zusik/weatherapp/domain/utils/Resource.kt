package com.ilia_zusik.weatherapp.domain.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<Nothing>: Resource<Nothing>()

    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(message: String) : Resource<T>(message = message)

    val isLoading get() = this is Loading
    val isError get() = (this as? Error)?.message
    val dataOrNull get() = (this as? Success)?.data
}