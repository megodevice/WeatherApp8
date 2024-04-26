package com.ilia_zusik.weatherapp.data.models.current

import com.google.gson.annotations.SerializedName

data class Temp(

    @SerializedName("temp")
    var temp: Double = 0.0,

    @SerializedName("feels_like")
    var feelsLike: Double = 0.0,

    @SerializedName("pressure")
    var pressure: Int = 0,

    @SerializedName("humidity")
    var humidity: Int = 0

)