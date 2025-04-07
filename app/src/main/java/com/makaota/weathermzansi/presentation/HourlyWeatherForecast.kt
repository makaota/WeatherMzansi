package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.domain.utils.ThemeColors
import java.time.LocalDateTime

@Composable
fun HourlyWeatherForecast(
    state: WeatherState,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        when {
            state.weatherInfo != null -> {
                Text(
                    text = "Hourly Forecast",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                   // modifier = Modifier.padding(start = 16.dp)
                )
                Box(
                    modifier = Modifier
                   //     .padding(16.dp)
                        .clip(RoundedCornerShape(10.dp)) // Apply rounded corners
                      //  .background(backgroundColor)    // Set background color
                ) {

                    state.weatherInfo?.weatherDataPerDay?.get(0)?.let { data ->

                        // Get the current time
                        val currentTime = remember { LocalDateTime.now() }

                        // Filter the data to show only the current hour onward
                        val todayData = remember(data, currentTime) {
                            data.filter { weatherData ->
                                Log.d("WeatherDataForecast", "hourlyWeatherForecast: $weatherData")
                                weatherData.time.isAfter(currentTime) || weatherData.time.hour == currentTime.hour ||
                                        weatherData.time.hour == 0

                            }

                        }
                        state.weatherInfo.weatherDataPerDay.get(1)?.let { data ->
                            val tomorrowData = data.sortedBy { it.time.hour }

                            val combinedData =
                                (todayData + tomorrowData).distinctBy { it.time.hour }

                            Log.d("combinedData", "values: $combinedData")

                            Column(
                                modifier = modifier
                                    .fillMaxWidth()
                                  //  .padding(horizontal = 16.dp)
                            ) {

                                Spacer(modifier = Modifier.height(16.dp))
                                LazyRow(userScrollEnabled = true,modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp)) // Apply rounded corners to LazyRow
                                    .background(backgroundColor)    // Ensure the background matches
                                    .padding(8.dp), content = {
                                    items(
                                        combinedData.sortedWith { weather1, weather2 ->
                                            val currentHour = currentTime.hour

                                            val hour1 = weather1.time.hour
                                            val hour2 = weather2.time.hour

                                            val adjustedHour1 =
                                                if (hour1 < currentHour) hour1 + 24 else hour1
                                            val adjustedHour2 =
                                                if (hour2 < currentHour) hour2 + 24 else hour2

                                            adjustedHour1.compareTo(adjustedHour2)
                                        }
                                            .take(24)
                                    ) { weatherData ->
                                        HourlyWeatherDisplay(
                                            weatherData = weatherData,
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .background(Color.Transparent),
                                            themeViewModel = themeViewModel
                                        )
                                    }

                                })
                            }
                        }
                    }
                }
            }
        }
    }

}



