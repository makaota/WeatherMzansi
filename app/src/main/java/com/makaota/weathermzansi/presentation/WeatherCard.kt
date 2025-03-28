package com.makaota.weathermzansi.presentation


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.utils.ThemeColors
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun WeatherCard(
    state: WeatherState,
    dailyState: DailyWeatherState,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    state.weatherInfo?.currentWeatherData?.let { data ->

        Log.d("WeatherCard", "WeatherData: $data")
        // Get the current time
        val currentTime = remember { LocalDateTime.now() }
        val formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        // Extract the first day's data for highs, lows, and rain chance
        val dailyWeatherData = dailyState.dailyWeatherInfo?.dailyWeatherData?.get(0)

        Log.d("WeatherCard", "DailyWeatherData: $dailyWeatherData")

        Card(
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor // Your desired background color
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display current time here
                Text(
                    text = "Today $formattedCurrentTime",
                    modifier = Modifier.align(Alignment.End),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = data.weatherType.iconRes),
                    contentDescription = null,
                    modifier = Modifier.width(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${data.temperatureCelsius}°C",
                    fontSize = 30.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = data.weatherType.weatherDesc,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(32.dp))
                // Display daily weather details
                dailyWeatherData?.let { dailyData ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_down),
                                contentDescription = null,
                                tint = Color.White, // Tinting the icon white
                                modifier = Modifier.size(24.dp) // Adjust size as needed
                            )
                            Text(
                                text = "${dailyData.get(0).lowTemperatures}°",
                                color = Color.White, // Text color to match
                                fontSize = 14.sp // Adjust font size as needed
                            )
                        }
                        Spacer(modifier = Modifier.width(15.dp))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_up),
                                contentDescription = null,
                                tint = Color.White, // Tinting the icon white
                                modifier = Modifier.size(24.dp) // Adjust size as needed
                            )
                            Text(
                                text = "${dailyData.get(0).maxTemperatures}°",
                                color = Color.White, // Text color to match
                                fontSize = 14.sp // Adjust font size as needed
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherDataDisplay(
                        value = data.pressure.roundToInt(),
                        unit = "hpa",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                        iconTint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                    WeatherDataDisplay(
                        value = data.humidity.roundToInt(),
                        unit = "%",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_drop),
                        iconTint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                    WeatherDataDisplay(
                        value = data.windSpeed.roundToInt(),
                        unit = "km/h",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_wind),
                        iconTint = Color.White,
                        textStyle = TextStyle(color = Color.White)
                    )
                }
            }
        }
    }
}

@Composable
fun WindInfoDisplay(
    state: WeatherState,
    modifier: Modifier = Modifier
) {


    val textColor = ThemeColors.textColor()
    val backgroundColor = ThemeColors.backgroundColor()
    val labelColor = ThemeColors.labelColor()

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
                    windSpeed = data.windSpeed
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
                    pressure = data.pressure.toFloat()
                )
            }
        }


//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            HumidityGauge(humidity = data.humidity.toFloat())
//            VisibilityBlurEffect(visibilityKm = data.visibility.toFloat())
//        }
    }

}

@Composable
fun HumidityInfoDisplay(
    state: WeatherState,
    modifier: Modifier = Modifier
) {

    val textColor = ThemeColors.textColor()
    val backgroundColor = ThemeColors.backgroundColor()
    val labelColor = ThemeColors.labelColor()


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
                    humidity = data.humidity.toFloat()
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
                VisibilityBlurEffect(
                    visibilityKm = data.visibility.toFloat(),data.dewPoint
                )
            }
        }

    }

}

