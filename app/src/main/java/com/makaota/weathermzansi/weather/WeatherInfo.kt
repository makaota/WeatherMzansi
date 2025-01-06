package com.makaota.weathermzansi.weather

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,
    val currentWeatherData: WeatherData?,
    //val weatherDataPerWeek: Map<Int, List<WeatherData>>
)