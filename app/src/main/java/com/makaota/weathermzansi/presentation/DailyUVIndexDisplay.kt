package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.domain.utils.ThemeColors


@Composable
fun DailyUVIndexDisplay( dailyState: DailyWeatherState,themeViewModel: ThemeViewModel) {


    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {


        when {
            dailyState.dailyWeatherInfo != null -> {
                Text("UV Index",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
                dailyState.dailyWeatherInfo?.dailyWeatherData?.let { data ->

                    val todayWeatherData = dailyState.dailyWeatherInfo.dailyWeatherData.get(0)

                    todayWeatherData?.let { todayData ->
                        UVIndexGradient(uvIndex = todayData.get(0).uvIndex.toFloat(),
                            themeViewModel = themeViewModel)
                    }

                }
            }
        }


    }
}

