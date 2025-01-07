package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.makaota.weathermzansi.weather.WeatherData
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeeklyWeatherForecast(
    weeklyWeatherData: List<List<WeatherData>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        weeklyWeatherData.forEachIndexed { dayIndex, dailyWeather ->

            val dayOfWeek = LocalDate.now()
                .plusDays(dayIndex.toLong()) // Calculate the day of the week
                .dayOfWeek
                .getDisplayName(TextStyle.FULL, Locale.getDefault()) // Get full name like "Monday"


            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = dayOfWeek,
                    color = Color.White,
                    modifier = Modifier.padding(start = 16.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(dailyWeather) { weatherData ->
                        HourlyWeatherDisplay(
                            weatherData = weatherData,
                            modifier = Modifier
                                .width(80.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
