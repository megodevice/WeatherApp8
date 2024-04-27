package com.ilia_zusik.weatherapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ilia_zusik.weatherapp.data.models.display.DisplayWeatherHourModel
import com.ilia_zusik.weatherapp.databinding.ItemWeatherBinding

class WeatherHourlyAdapter :
    ListAdapter<DisplayWeatherHourModel, WeatherHourlyAdapter.WeatherStepViewHolder>(DIFF_UTIL_CALLBACK) {

    private companion object {
        val DIFF_UTIL_CALLBACK: DiffUtil.ItemCallback<DisplayWeatherHourModel> =
            object : DiffUtil.ItemCallback<DisplayWeatherHourModel>() {
                override fun areItemsTheSame(oldItem: DisplayWeatherHourModel, newItem: DisplayWeatherHourModel): Boolean {
                    return oldItem.hour == newItem.hour
                }

                override fun areContentsTheSame(
                    oldItem: DisplayWeatherHourModel,
                    newItem: DisplayWeatherHourModel
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

        fun onBind(hour: DisplayWeatherHourModel) {
            binding.apply {
                tvTime.text = hour.hour
                tvRainPercent.text = hour.rainPercentage
                ivImage.load(hour.imageUrl)
            }
        }
    }
}