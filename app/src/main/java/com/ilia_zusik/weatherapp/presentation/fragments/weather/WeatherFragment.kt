package com.ilia_zusik.weatherapp.presentation.fragments.weather

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilia_zusik.weatherapp.databinding.FragmentWeatherBinding
import com.ilia_zusik.weatherapp.presentation.adapters.WeatherHourlyAdapter
import com.ilia_zusik.weatherapp.presentation.fragments.base.BaseFragment
import com.ilia_zusik.weatherapp.presentation.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class WeatherFragment : BaseFragment<
        FragmentWeatherBinding, WeatherViewModel>
    (FragmentWeatherBinding::inflate) {

    override val viewModel: WeatherViewModel by viewModels()
    private val adapter: WeatherHourlyAdapter by lazy {
        WeatherHourlyAdapter()
    }

    override fun observe() {
        super.observe()
        viewModel.data.onEach {
            binding.apply {
                it?.let { weather ->
                    tvCityFull.text = weather.cityName
                    tvCityInitial.text = weather.cityInitial
                    tvTemp.text = weather.temperature
                    tvDay.text = weather.date
                    adapter.submitList(weather.hourly)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isFail.onEach {
            it?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.isLoading.onEach {
            binding.animLoading.isVisible = it
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun initialize() = with(binding) {
        super.initialize()
        rvHourly.adapter = adapter
        viewModel.submit("Bishkek")
    }
}