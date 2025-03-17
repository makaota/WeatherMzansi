package com.makaota.weathermzansi.data.remote

import com.squareup.moshi.Json

data class WeatherDataDto(
    val time: List<String>,
    @field:Json(name = "temperature_2m")
    val temperatures: List<Double>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "pressure_msl")
    val pressures: List<Double>,
    @field:Json(name = "windspeed_10m")
    val windSpeeds: List<Double>,
    @field:Json(name = "relativehumidity_2m")
    val humidities: List<Double>,
    @field:Json(name = "apparent_temperature")
    val feelsLike: List<Double>,
    @field:Json(name = "visibility")
    val visibility: List<Double>,
    @field:Json(name = "wind_direction_10m")
    val windDirection: List<Double>,
    @field:Json(name = "precipitation_probability")
    val precipitationProbability: List<Double>,
    @field:Json(name = "dew_point_2m")
    val dewPoint: List<Double>
)

