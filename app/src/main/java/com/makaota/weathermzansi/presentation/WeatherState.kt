package com.makaota.weathermzansi.presentation

import com.makaota.weathermzansi.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
