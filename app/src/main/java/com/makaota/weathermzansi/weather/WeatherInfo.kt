package com.makaota.weathermzansi.weather

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?,
    val weeklyWeatherData: List<List<WeatherData>> // Weekly data grouped per day

)