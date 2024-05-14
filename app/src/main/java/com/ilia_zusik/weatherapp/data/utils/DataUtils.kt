package com.ilia_zusik.weatherapp.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DataUtils {

    fun convertDate(millis: Long): String =
        SimpleDateFormat("MM.dd - HH:mm", Locale.ROOT).format(Date(millis))

    fun getCityInitial(cityName: String) =
        cityName.substring(0, 3).uppercase(Locale.ROOT)

    fun getRainPercentage(pop: Double) = "${(pop * 100).toInt()}%"

    fun getImageUrl(imageId: String) = "https://openweathermap.org/img/wn/$imageId@2x.png"

}