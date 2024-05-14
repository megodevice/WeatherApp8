package com.ilia_zusik.weatherapp.presentation.fragments.weather

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ilia_zusik.weatherapp.databinding.FragmentWeatherBinding
import com.ilia_zusik.weatherapp.presentation.adapters.WeatherHourlyAdapter
import com.ilia_zusik.weatherapp.presentation.fragments.base.BaseFragment
import com.ilia_zusik.weatherapp.domain.utils.UiResource
import com.ilia_zusik.weatherapp.presentation.viewModels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {
            viewModel.weather.collect { uiResource ->
                binding.animLoading.isVisible = uiResource is UiResource.Loading
                when (uiResource) {
                    is UiResource.Error -> {
                        Toast.makeText(requireContext(), uiResource.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    is UiResource.Loading -> {}
                    is UiResource.Success -> {
                        binding.apply {
                            uiResource.data?.let { weather ->
                                tvCityFull.text = weather.cityName
                                tvCityInitial.text = weather.cityInitial
                                tvTemp.text = weather.temperature
                                tvDay.text = weather.date
                                adapter.submitList(weather.hourly)
                            }
                        }

                    }
                }

            }
        }
    }

    override fun initialize() {
        super.initialize()
        binding.rvHourly.adapter = adapter
        viewModel.getWeather("Bishkek")
    }
}