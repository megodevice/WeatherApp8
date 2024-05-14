package com.ilia_zusik.weatherapp.di

import com.ilia_zusik.weatherapp.data.remote.WeatherApi
import com.ilia_zusik.weatherapp.data.repositories.WeatherRepositoryImpl
import com.ilia_zusik.weatherapp.data.room.WeatherDao
import com.ilia_zusik.weatherapp.domain.repositories.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideWeatherRepository(api: WeatherApi, dao: WeatherDao): WeatherRepository =
        WeatherRepositoryImpl(api, dao)


}