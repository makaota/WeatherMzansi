package com.makaota.weathermzansi.presentation

import android.util.Log
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
import java.time.Duration


@Composable
fun DailyDurationDisplay( dailyState: DailyWeatherState,) {


    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {


        when {
            dailyState.dailyWeatherInfo != null -> {
                Text("Duration",
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier.padding(start = 16.dp))
                //  Spacer(modifier = Modifier.height(16.dp))
                dailyState.dailyWeatherInfo?.dailyWeatherData?.let { data ->

                    val todayWeatherData = dailyState.dailyWeatherInfo.dailyWeatherData.get(0)

                    todayWeatherData?.let { todayData ->
                        DaylightDurationLayout(sunriseTime = todayData.get(0).sunrise,
                            sunsetTime = todayData.get(0).sunset,
                            daylightDuration = Duration.ofSeconds(todayData.get(0).daylightDuration.toLong()))
                        Log.d("DailyDisplay", "WeatherData: $data")
                        Log.d("DailyDisplay", "TodayData: $todayData")
                    }

                }
            }
        }


    }
}

