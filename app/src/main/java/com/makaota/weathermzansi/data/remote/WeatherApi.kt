package com.makaota.weathermzansi.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl,apparent_temperature,precipitation_probability&timezone=Africa/Johannesburg")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDto

    @GET("v1/forecast?daily=temperature_2m_max,temperature_2m_min,precipitation_sum,weathercode,sunrise,sunset,daylight_duration&timezone=Africa/Johannesburg")
    suspend fun getDailyWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): DailyWeatherDto
}