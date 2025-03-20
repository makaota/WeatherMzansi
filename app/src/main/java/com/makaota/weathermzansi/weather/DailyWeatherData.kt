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
    val uvIndex: Double,
    val daylightDuration: Double,
    val apparentTemperatureMax: Double,
    val apparentTemperatureMin: Double,
    val sunshineDuration: Double,
    val uvIndexClearSky: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val windGust: Double,
    val snowfallSum: Double,
    val weatherType: WeatherType
)

