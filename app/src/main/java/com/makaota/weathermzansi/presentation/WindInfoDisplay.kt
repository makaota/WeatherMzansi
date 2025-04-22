package com.makaota.weathermzansi.presentation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.makaota.weathermzansi.domain.utils.ThemeColors

@Composable
fun WindInfoDisplay(
    state: WeatherState,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    state.weatherInfo?.currentWeatherData?.let { data ->

        Text(
            text = "Current Weather Details",
            fontSize = 20.sp,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier= Modifier.padding(16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 180.dp, max = 230.dp) // Adjust height dynamically

            ) {
                WindDirectionCompass(
                    windDegrees = data.windDirection.toFloat(),
                    windSpeed = data.windSpeed.dec(),
                    themeViewModel = themeViewModel
                )
            }

            Spacer(modifier = Modifier.width(10.dp)) // Space between two cards

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 180.dp, max = 230.dp) // Adjust height dynamically

            ) {
                PressureGauge(
                    pressure = data.pressure.toFloat(),
                    themeViewModel = themeViewModel
                )
            }
        }

    }

}

@Composable
fun HumidityInfoDisplay(
    state: WeatherState,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)


    state.weatherInfo?.currentWeatherData?.let { data ->

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 180.dp, max = 230.dp) // Adjust height dynamically

            ) {
                HumidityGauge(
                    humidity = data.humidity.toFloat(),
                    themeViewModel = themeViewModel
                )
            }

            Spacer(modifier = Modifier.width(10.dp)) // Space between two cards

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(230.dp)

            ) {
                VisibilityBlurEffect(
                    visibilityKm = data.visibility.toFloat(), themeViewModel = themeViewModel
                )
            }
        }

    }

}

