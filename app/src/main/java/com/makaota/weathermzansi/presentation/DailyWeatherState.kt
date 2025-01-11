package com.makaota.weathermzansi.presentation

import com.makaota.weathermzansi.weather.DailyWeatherInfo


data class DailyWeatherState(
    val dailyWeatherInfo: DailyWeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
