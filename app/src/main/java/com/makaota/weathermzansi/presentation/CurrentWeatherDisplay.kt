package com.makaota.weathermzansi.presentation


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun CurrentWeatherDisplay(
    hourlyState: WeatherState,
    dailyState: DailyWeatherState,
    modifier: Modifier = Modifier,
) {
    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(id = R.color.white)

    val labelColor = if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
    else colorResource(id = R.color.medium_gray)

    val backgroundColor = if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
    else colorResource(id = R.color.sky_blue)

    hourlyState.weatherInfo?.currentWeatherData?.let { data ->
        Log.d("WeatherCard", "WeatherData: $data")

        // Get the current time
        val currentTime = remember { LocalDateTime.now() }
        val formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        // Extract first day's highs, lows, and rain chance
        val dailyWeatherData = dailyState.dailyWeatherInfo?.dailyWeatherData?.get(0)

        Log.d("WeatherCard", "DailyWeatherData: $dailyWeatherData")

        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight() // Fill the full height to push content to bottom
                .background(Color.Transparent)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom // Push all content to the bottom
        ) {
                Text(
                    text = data.weatherType.weatherDesc,
                    fontSize = 18.sp,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally)

                )

            Text(
                text = "Now $formattedCurrentTime",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp)) // Space before weather details

            // Temperature and Weather Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${data.temperatureCelsius.roundToInt()}°",
                    fontSize = 60.sp,
                    color = textColor,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = data.weatherType.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp)
                )


            Spacer(modifier = Modifier.weight(1f)) // Space before weather description

            // Weather description and Feels Like
            Column(horizontalAlignment = Alignment.End) {

                Text(
                    text = "Feels Like ${data.feelsLike.roundToInt()}°",
                    fontSize = 18.sp,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }

            Spacer(modifier = Modifier.height(12.dp)) // Space before high/low temperatures

            // High and Low Temperatures
            dailyWeatherData?.let { dailyData ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrow_up),
                            contentDescription = null,
                            tint = colorResource(id = R.color.orange_red)
                        )
                        Text(
                            text = "${dailyData.get(0).maxTemperatures.roundToInt()}°",
                            color = textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            contentDescription = null,
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrow_down),
                            tint = colorResource(id = R.color.dodger_blue)
                        )
                        Text(
                            text = "${dailyData.get(0).lowTemperatures.roundToInt()}°",
                            color = textColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
