package com.makaota.weathermzansi.weather

import java.time.LocalDate
import java.time.LocalTime

data class DailyWeatherData(
    val time: LocalDate,
    val maxTemperatures: Double,
    val lowTemperatures: Double,
    val chancesOfRain: Double,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val daylightDuration: Double,
    val weatherType: WeatherType
)

