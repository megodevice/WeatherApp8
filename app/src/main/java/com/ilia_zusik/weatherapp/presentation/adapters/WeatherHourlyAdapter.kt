package com.ilia_zusik.weatherapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ilia_zusik.weatherapp.data.models.hours.WeatherStep
import com.ilia_zusik.weatherapp.databinding.ItemWeatherBinding

class WeatherHourlyAdapter :
    ListAdapter<WeatherStep, WeatherHourlyAdapter.WeatherStepViewHolder>(DIFF_UTIL_CALLBACK) {

    private companion object {
        val DIFF_UTIL_CALLBACK: DiffUtil.ItemCallback<WeatherStep> =
            object : DiffUtil.ItemCallback<WeatherStep>() {
                override fun areItemsTheSame(oldItem: WeatherStep, newItem: WeatherStep): Boolean {
                    return oldItem.dt == newItem.dt
                }

                override fun areContentsTheSame(
                    oldItem: WeatherStep,
                    newItem: WeatherStep
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherStepViewHolder {
        return WeatherStepViewHolder(
            ItemWeatherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WeatherStepViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class WeatherStepViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(weatherStep: WeatherStep) {
            binding.apply {
                tvTime.text = weatherStep.dt_txt.substring(11, 16)
                "${(weatherStep.pop * 100).toInt()}%".apply { tvRainPercent.text = this }
                ivImage.load("https://openweathermap.org/img/wn/"
                        + weatherStep.weather[0].icon + "@2x.png")
            }
        }

    }
}