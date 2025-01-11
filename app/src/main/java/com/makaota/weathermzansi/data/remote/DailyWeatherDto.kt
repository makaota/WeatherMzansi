package com.makaota.weathermzansi.data.remote

import com.squareup.moshi.Json

data class DailyWeatherDto (
    @field:Json(name = "daily")
    val dailyWeatherData: DailyWeatherDataDto
)