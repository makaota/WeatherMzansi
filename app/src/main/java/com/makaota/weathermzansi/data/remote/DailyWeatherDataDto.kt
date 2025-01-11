package com.makaota.weathermzansi.data.remote

import com.squareup.moshi.Json

data class DailyWeatherDataDto (
    val time: List<String>,
    @field:Json(name = "weathercode")
    val weatherCodes: List<Int>,
    @field:Json(name = "temperature_2m_max")
    val maxTemperatures: List<Double>,
    @field:Json(name = "temperature_2m_min")
    val lowTemperatures: List<Double>,
    @field:Json(name = "precipitation_sum")
    val chancesOfRain: List<Double>
)