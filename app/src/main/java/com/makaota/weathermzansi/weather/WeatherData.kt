package com.makaota.weathermzansi.weather

import java.time.LocalDateTime

data class WeatherData(
    val time: LocalDateTime,
    val temperatureCelsius: Double,
    val pressure: Double,
    val windSpeed: Double,
    val humidity: Double,
    val feelsLike: Double,
    val precipitationProbability: Double,
    val weatherType: WeatherType
)