package com.ilia_zusik.weatherapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ilia_zusik.weatherapp.data.models.room.RoomWeather
import com.ilia_zusik.weatherapp.data.models.room.RoomWeatherHour

@Database(entities = [RoomWeather::class, RoomWeatherHour::class], version = 1)
abstract class WeatherDb : RoomDatabase() {

    abstract fun dao(): WeatherDao

}