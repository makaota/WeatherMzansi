package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DailyWeatherForecast(
    state: DailyWeatherState,
    modifier: Modifier = Modifier
) {
    // Ensure there's data available to work with
    state.dailyWeatherInfo?.dailyWeatherData?.get(0)?.let { data ->

        // Format the date to show day and date
     //   val formattedDate = remember(data) {
           // data.format(DateTimeFormatter.ofPattern("EEE, MMM d")) // Formatting day and date
            data.get(0).time.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
      //  }

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

            // Pass the `data` to DailyWeatherDisplay composable
            DailyWeatherDisplay(
                dailyWeatherData = data.get(0), // Pass the actual weather data for today
                modifier = Modifier
                    .height(100.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }

}
