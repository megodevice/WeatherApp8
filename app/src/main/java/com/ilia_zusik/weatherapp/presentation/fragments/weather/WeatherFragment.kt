package com.ilia_zusik.weatherapp.presentation.fragments.weather

import androidx.fragment.app.viewModels
import com.ilia_zusik.weatherapp.databinding.FragmentWeatherBinding
import com.ilia_zusik.weatherapp.presentation.adapters.WeatherHourlyAdapter
import com.ilia_zusik.weatherapp.presentation.fragments.base.BaseFragment
import com.ilia_zusik.weatherapp.presentation.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        viewModel.weather().resHandler({}, {weather ->
            tvCityFull.text = weather.cityName
            tvCityInitial.text = weather.cityInitial
            tvTemp.text = weather.temperature
            adapter.submitList(weather.hourly)
        })

    }

    override fun initialize() = with(binding) {
        super.initialize()
        rvHourly.adapter = adapter
    }

}