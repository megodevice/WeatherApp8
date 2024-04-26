package com.ilia_zusik.weatherapp.presentation.fragments.weather

import androidx.fragment.app.viewModels
import com.ilia_zusik.weatherapp.databinding.FragmentWeatherBinding
import com.ilia_zusik.weatherapp.presentation.adapters.WeatherHourlyAdapter
import com.ilia_zusik.weatherapp.presentation.fragments.base.BaseFragment
import com.ilia_zusik.weatherapp.presentation.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class WeatherFragment : BaseFragment<
        FragmentWeatherBinding, WeatherViewModel>
    (FragmentWeatherBinding::inflate) {

    override val viewModel: WeatherViewModel by viewModels()
    private val adapter: WeatherHourlyAdapter by lazy {
        WeatherHourlyAdapter()
    }

    override fun observe() = with(binding) {
        super.observe()
        viewModel.getWeather().resHandler({ isLoading ->

        }, { weatherModel ->
            tvCityFull.text = weatherModel.name
            tvCityInitial.text = weatherModel.name.substring(0, 3).uppercase(Locale.ROOT)
            "${weatherModel.temp.temp.toInt()}°".apply {
                tvTemp.text = this
            }
            setHourlyWeather(weatherModel.id)
        })
    }

    private fun setHourlyWeather(weatherId: Int) {
        viewModel.getHourlyWeather(weatherId).resHandler({ isLoading ->

        }, { hoursWeatherModel ->
            adapter.submitList(hoursWeatherModel.list)
        })
    }

    override fun initialize() = with(binding) {
        super.initialize()
        rvHourly.adapter = adapter
    }

}