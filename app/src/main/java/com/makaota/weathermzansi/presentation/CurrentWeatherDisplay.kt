package com.makaota.weathermzansi.presentation


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    state: WeatherState,
    dailyState: DailyWeatherState,
    modifier: Modifier = Modifier,
) {

    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )

    val labelColor = if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
    else colorResource(id = R.color.medium_gray)

    val backgroundColor = if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
    else colorResource(
        id = R.color.sky_blue
    )



    state.weatherInfo?.currentWeatherData?.let { data ->

        Log.d("WeatherCard", "WeatherData: $data")
        // Get the current time
        val currentTime = remember { LocalDateTime.now() }
        val formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        // Extract the first day's data for highs, lows, and rain chance
        val dailyWeatherData = dailyState.dailyWeatherInfo?.dailyWeatherData?.get(0)

        Log.d("WeatherCard", "DailyWeatherData: $dailyWeatherData")


        Column(
            modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp),
        ) {
            Text(
                text = "Now $formattedCurrentTime",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${data.temperatureCelsius.roundToInt()}°",
                        fontSize = 50.sp,
                        color = textColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = data.weatherType.iconRes),
                        contentDescription = null,
                        modifier = Modifier.width(45.dp)

                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = data.weatherType.weatherDesc,
                        fontSize = 18.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Feels Like ${data.feelsLike.roundToInt()}°",
                        fontSize = 18.sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
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
                            tint = colorResource(id = R.color.orange_red),

                            )
                        Text(
                            text = "${dailyData.get(0).maxTemperatures.roundToInt()}°",
                            color = labelColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )

                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Row(horizontalArrangement = Arrangement.Center) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.arrow_down),
                            contentDescription = null,
                            tint = colorResource(id = R.color.dodger_blue),

                            )
                        Text(
                            text = "${dailyData.get(0).lowTemperatures.roundToInt()}°",
                            color = labelColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }

        }
    }
}





