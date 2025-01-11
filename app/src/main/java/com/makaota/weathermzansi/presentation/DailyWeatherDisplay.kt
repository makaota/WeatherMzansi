package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.weather.DailyWeatherData
import com.makaota.weathermzansi.weather.WeatherType
import java.time.LocalDate
import java.time.LocalDate.*
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DailyWeatherDisplay(
    dailyWeatherData: DailyWeatherData,
    modifier: Modifier = Modifier,
    textColor: Color = Color.White,
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

    val day = remember(dailyWeatherData) { dailyWeatherData.time.format(dayFormatter) }
    val date = remember(dailyWeatherData) { dailyWeatherData.time.format(dateFormatter) }


    Row(
        modifier = modifier
            .height(100.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = day, color = Color.LightGray, fontWeight = FontWeight.Bold)
            Text(text = date, color = Color.LightGray)
        }

        Image(
            painter = painterResource(id = R.drawable.ic_rainy),
            contentDescription = "Rainy Weather Icon",
            modifier = Modifier.size(25.dp)
        )
        Text(
            text = "${dailyWeatherData.chancesOfRain.roundToInt()}%",
            color = Color.LightGray,
            fontSize = 12.sp
        )
        Image(
            painter = painterResource(id = dailyWeatherData.weatherType.iconRes),
            contentDescription = null,
            modifier = Modifier.width(25.dp)
        )
        Text(
            text = dailyWeatherData.weatherType.weatherDesc,
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = "${dailyWeatherData.lowTemperatures}°C / ${dailyWeatherData.maxTemperatures}°C",
            color = textColor,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDailyWeatherDisplay() {
    val sampleData = DailyWeatherData(
        time = of(2025, 1, 10), // Example date
        maxTemperatures = 28.0,
        lowTemperatures = 18.0,
        chancesOfRain = 20.0,
        weatherType = WeatherType.ClearSky // Ensure WeatherType.Sunny has a valid iconRes
    )

    DailyWeatherDisplay(
        dailyWeatherData = sampleData,
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF121212))
            .fillMaxWidth(),
        textColor = Color.White
    )
}

