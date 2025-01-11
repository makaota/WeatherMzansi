package com.makaota.weathermzansi.data.repository

import com.makaota.weathermzansi.data.mappers.toDailyWeatherInfo
import com.makaota.weathermzansi.data.mappers.toWeatherInfo
import com.makaota.weathermzansi.data.remote.WeatherApi
import com.makaota.weathermzansi.domain.repository.WeatherRepository
import com.makaota.weathermzansi.utils.Resource
import com.makaota.weathermzansi.weather.DailyWeatherInfo
import com.makaota.weathermzansi.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo()
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun getDailyWeatherData(lat: Double, long: Double): Resource<DailyWeatherInfo> {
        return try {
            Resource.Success(
                data = api.getDailyWeatherData(
                    lat = lat,
                    long = long
                ).toDailyWeatherInfo()
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}