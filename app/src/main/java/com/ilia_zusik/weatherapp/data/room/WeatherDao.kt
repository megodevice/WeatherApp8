package com.ilia_zusik.weatherapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ilia_zusik.weatherapp.data.models.room.RoomWeather
import com.ilia_zusik.weatherapp.data.models.room.RoomWeatherHour

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    suspend fun getWeather() : RoomWeather?

    @Query("SELECT * FROM hours")
    suspend fun getHourly() : List<RoomWeatherHour>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeather(roomWeather: RoomWeather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHours(hourly: ArrayList<RoomWeatherHour>)

    @Query("DELETE FROM weather")
    suspend fun clearWeather()

    @Query("DELETE FROM hours")
    suspend fun clearHours()

}