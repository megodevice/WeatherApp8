package com.ilia_zusik.weatherapp.data.models.current

import com.google.gson.annotations.SerializedName

data class WeatherModel(

    @SerializedName("weather")
    var weather: ArrayList<Weather> = arrayListOf(),

    @SerializedName("main")
    var temp: Temp = Temp(),

    @SerializedName("visibility")
    var visibility: Int = 0,

    @SerializedName("wind")
    var wind: Wind = Wind(),

    @SerializedName("name")
    var name: String = String(),

    @SerializedName("dt")
    var dateUnix: Long = 0,

    @SerializedName("timezone")
    var timeZoneOffset: Long,

    @SerializedName("id")
    var id: Int = 0

)