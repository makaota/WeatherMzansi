package com.makaota.weathermzansi.weather

import java.time.LocalDate

data class DailyWeatherData (
    val time: LocalDate,
    val maxTemperatures: Double,
    val lowTemperatures: Double,
    val chancesOfRain: Double,
    val weatherType: WeatherType
)

