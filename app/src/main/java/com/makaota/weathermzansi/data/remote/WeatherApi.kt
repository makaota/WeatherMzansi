package com.makaota.weathermzansi.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("v1/forecast?hourly=temperature_2m," +
            "weathercode," +
            "relativehumidity_2m," +
            "windspeed_10m," +
            "pressure_msl," +
            "apparent_temperature," +
            "wind_direction_10m," +
            "visibility," +
            "dew_point_2m," +
            "precipitation_probability&timezone=Africa/Johannesburg")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDto

    @GET("v1/forecast?daily=temperature_2m_max," +
            "temperature_2m_min," +
            "precipitation_sum," +
            "weathercode," +
            "sunrise,sunset," +
            "uv_index_max," +
            "daylight_duration," +
            "apparent_temperature_max," +
            "apparent_temperature_min," +
            "sunshine_duration," +
            "uv_index_clear_sky_max," +
            "snowfall_sum," +
            "wind_speed_10m_max," +
            "wind_direction_10m_dominant," +
            "wind_gusts_10m_max&timezone=Africa/Johannesburg")
    suspend fun getDailyWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): DailyWeatherDto
}


// Key for Maps SDK AIzaSyBLByYu8IQon0go5ngsHHgU_edGX58NXjU