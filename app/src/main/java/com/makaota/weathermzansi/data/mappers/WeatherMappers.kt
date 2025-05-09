package com.makaota.weathermzansi.data.mappers

import android.util.Log
import com.makaota.weathermzansi.data.remote.DailyWeatherDataDto
import com.makaota.weathermzansi.data.remote.DailyWeatherDto
import com.makaota.weathermzansi.data.remote.WeatherDataDto
import com.makaota.weathermzansi.data.remote.WeatherDto
import com.makaota.weathermzansi.weather.DailyWeatherData
import com.makaota.weathermzansi.weather.DailyWeatherInfo
import com.makaota.weathermzansi.weather.WeatherData
import com.makaota.weathermzansi.weather.WeatherInfo
import com.makaota.weathermzansi.weather.WeatherType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

private data class IndexedDailyWeatherData(
    val index: Int,
    val data: DailyWeatherData
)
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        val feelsLike = feelsLike[index]
        val windDirection = windDirection[index]
        val visibility = visibility[index]
        val dewPoint = dewPoint[index]
        val precipitationProbability = precipitationProbability[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                feelsLike = feelsLike,
                windDirection = windDirection,
                visibility = visibility,
                precipitationProbability = precipitationProbability,
                dewPoint = dewPoint,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        Log.d("WeatherDataMap", "Grouped values: ${it.value}")
        it.value.map { it.data }
    }.also {
        Log.d("WeatherDataMap", "Grouped keys: ${it.keys}")

    }
}


fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()
    val currentHourWeatherData = weatherDataMap[0]?.find {
        val hour = if (now.minute < 30) now.hour else (now.hour + 1) % 24
        it.time.hour == hour
    }

    Log.d("WeatherDataMap", "Grouped hours: $weatherDataMap")
    Log.d("WeatherDataMap", "currentHourlyWeatherData: $currentHourWeatherData")

    // Extract weekly weather data
    val weeklyWeatherData = weatherDataMap.values.toList()
    Log.d("weeklyWeatherData", "weeklyWeatherData: $weeklyWeatherData")
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentHourWeatherData,
        weeklyWeatherData = weeklyWeatherData

    )

}


fun DailyWeatherDataDto.toDailyWeatherDataMap(): Map<Int, List<DailyWeatherData>> {
    return time.mapIndexed { index, time ->
        val date = LocalDate.parse(time, DateTimeFormatter.ISO_DATE)
        val weatherCodes = weatherCodes[index]
        val maxTemperatures = maxTemperatures[index]
        val lowTemperatures = lowTemperatures[index]
        val chancesOfRain = chancesOfRain[index]
        val sunriseTime = sunrise[index]
        val sunsetTime = sunset[index]
        val uvIndex = uvIndex[index]
        val daiylightDuration = daylightDuration[index]
        val apparentTemperatureMax = apparentTemperatureMax[index]
        val apparentTemperatureMin = apparentTemperatureMin[index]
        val sunshineDuration = sunshineDuration[index]
        val uvIndexClearSky = uvIndexClearSkyMax[index]
        val snowfallSum = snowfallSum[index]
        val windSpeed = windSpeedMax[index]
        val windDirection = windDirectionDominant[index]
        val windGust = windGutsMax[index]
        IndexedDailyWeatherData(
            index = index,
            data = DailyWeatherData(
                time = date,
                maxTemperatures = maxTemperatures,
                lowTemperatures = lowTemperatures,
                chancesOfRain = chancesOfRain,
                sunrise = LocalDateTime.parse(sunriseTime, DateTimeFormatter.ISO_DATE_TIME).toLocalTime(),
                sunset = LocalDateTime.parse(sunsetTime, DateTimeFormatter.ISO_DATE_TIME).toLocalTime(),
                uvIndex = uvIndex,
                daylightDuration = daiylightDuration,
                apparentTemperatureMax = apparentTemperatureMax,
                apparentTemperatureMin = apparentTemperatureMin,
                sunshineDuration = sunshineDuration,
                uvIndexClearSky = uvIndexClearSky,
                snowfallSum = snowfallSum,
                windSpeed = windSpeed,
                windDirection = windDirection,
                windGust = windGust,
                weatherType = WeatherType.fromWMO(weatherCodes)
            )
        )
    }.groupBy {
        it.index
    }.mapValues {
        Log.d("DailyWeatherDataMap", "Grouped values: ${it.value}")
        it.value.map { it.data }
    }.also {
        Log.d("DailyWeatherDataMap", "Grouped keys: ${it.keys}")

    }
}

fun DailyWeatherDto.toDailyWeatherInfo(): DailyWeatherInfo {
    val dailyWeatherDataMap = dailyWeatherData.toDailyWeatherDataMap()

    Log.d("DailyWeatherDataMap", "Grouped days: $dailyWeatherDataMap")
    return DailyWeatherInfo(
        dailyWeatherData = dailyWeatherDataMap,

    )
}