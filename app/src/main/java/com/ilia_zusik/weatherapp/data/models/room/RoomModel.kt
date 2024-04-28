package com.ilia_zusik.weatherapp.data.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class RoomWeather(
    val cityName: String,
    val cityInitial: String,
    val temperature: String,
    val dateUnix: Long
)

@Entity(tableName = "hours")
data class RoomWeatherHour(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val hour: String,
    val rainPercentage: String,
    val imageUrl: String,
    val dateUnix: Long
)