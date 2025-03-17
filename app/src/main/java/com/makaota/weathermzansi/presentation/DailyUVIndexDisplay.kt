package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R


@Composable
fun DailyUVIndexDisplay( dailyState: DailyWeatherState,) {


    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {


        when {
            dailyState.dailyWeatherInfo != null -> {
                Text("UV Index",
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                )
                //  Spacer(modifier = Modifier.height(16.dp))
                dailyState.dailyWeatherInfo?.dailyWeatherData?.let { data ->

                    val todayWeatherData = dailyState.dailyWeatherInfo.dailyWeatherData.get(0)

                    todayWeatherData?.let { todayData ->
                        UVIndexGradient(uvIndex = todayData.get(0).uvIndex.toFloat())
                     //   UVIndexSunIcon(uvIndex = todayData.get(0).uvIndex.toFloat())

                    }

                }
            }
        }


    }
}

