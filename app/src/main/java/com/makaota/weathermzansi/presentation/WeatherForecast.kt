package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.ui.theme.DeepBlue
import java.time.LocalDateTime

@Composable
fun WeatherForecast(
    state: WeatherState,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {
    state.weatherInfo?.weatherDataPerDay?.get(0)?.let { data ->

        // Get the current time
        val currentTime = remember { LocalDateTime.now() }

        // Filter the data to show only the current hour onward
        val filteredData = remember(data, currentTime) {
            data.filter { weatherData ->
                Log.d("WeatherDataForecast", "values: $weatherData")
                weatherData.time.isAfter(currentTime) || weatherData.time.hour == currentTime.hour  ||
                        weatherData.time.hour == 0

            }

        }

        Log.d("filteredData", "values: ${filteredData.size}")

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Today",
                fontSize = 20.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(content = {
                items(
                    filteredData.sortedWith { weather1, weather2 ->
                        val currentHour = currentTime.hour

                        val hour1 = weather1.time.hour
                        val hour2 = weather2.time.hour

                        val adjustedHour1 = if (hour1 < currentHour) hour1 + 24 else hour1
                        val adjustedHour2 = if (hour2 < currentHour) hour2 + 24 else hour2

                        adjustedHour1.compareTo(adjustedHour2)
                    }
                ) { weatherData ->
                    HourlyWeatherDisplay(
                        weatherData = weatherData,
                        modifier = Modifier
                            .height(100.dp)
                            .padding(horizontal = 16.dp)
                            .background(DeepBlue),
                        themeViewModel = themeViewModel
                    )
                }


            })
        }
    }
}
