package com.ilia_zusik.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.ilia_zusik.weatherapp.data.room.WeatherDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DbModule {

    @Provides
    @Singleton
    fun provideWeatherDb(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        context = appContext,
        klass = WeatherDb::class.java,
        name = "history"
    ).build()

    @Provides
    @Singleton
    fun provideWeatherDao(weatherDb: WeatherDb) =
        weatherDb.dao()

}