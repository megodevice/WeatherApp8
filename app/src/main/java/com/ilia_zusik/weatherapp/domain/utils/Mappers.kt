package com.ilia_zusik.weatherapp.domain.utils

import com.ilia_zusik.weatherapp.data.models.current.WeatherModel
import com.ilia_zusik.weatherapp.data.models.hours.WeatherHourModel
import com.ilia_zusik.weatherapp.data.utils.DataUtils
import com.ilia_zusik.weatherapp.domain.models.DisplayWeatherHourModel
import com.ilia_zusik.weatherapp.domain.models.DisplayWeatherModel

fun WeatherModel.toDisplayWeatherModel(hourly: List<DisplayWeatherHourModel>): DisplayWeatherModel {
    return DisplayWeatherModel(
        cityName = name,
        cityInitial = DataUtils.getCityInitial(name),
        temperature = "${temp.temp.toInt()}°",
        hourly = hourly,
        date = DataUtils.convertDate(dateUnix * 1000 + timeZoneOffset)
    )
}

fun WeatherHourModel.toDisplayWeatherHourModel(): DisplayWeatherHourModel {
    return DisplayWeatherHourModel(
        hour = this.dt_txt.substring(11, 16),
        rainPercentage = DataUtils.getRainPercentage(this.pop),
        imageUrl = DataUtils.getImageUrl(this.weather[0].icon)
    )
}