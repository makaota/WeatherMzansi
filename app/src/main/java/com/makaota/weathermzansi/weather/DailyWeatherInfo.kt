package com.makaota.weathermzansi.weather

data class DailyWeatherInfo (
    val dailyWeatherData: Map<Int, List<DailyWeatherData>>
)