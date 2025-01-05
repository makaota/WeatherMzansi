package com.makaota.weathermzansi.domain.repository

import com.makaota.weathermzansi.utils.Resource
import com.makaota.weathermzansi.weather.WeatherInfo

interface WeatherRepository {

    suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo>
}