package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.weather.DailyWeatherData
import com.makaota.weathermzansi.weather.WeatherType
import java.time.LocalDate.of
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DailyWeatherDisplay(
    dailyWeatherData: DailyWeatherData,
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


    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

    val day = remember(dailyWeatherData) { dailyWeatherData.time.format(dayFormatter) }
    val date = remember(dailyWeatherData) { dailyWeatherData.time.format(dateFormatter) }


    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)) // Apply rounded corners
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .width(60.dp) // Fixed width for day and date
        ) {
            Text(
                text = day,
                color = labelColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = date,
                color =textColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.width(40.dp), // Fixed width for chances of rain

        ) {

            Image(
                painter = painterResource(id = dailyWeatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(25.dp)

            )

            Text(
                text = "${dailyWeatherData.chancesOfRain.roundToInt()}%",
                color = colorResource(id = R.color.dodger_blue),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

        }

        Text(
            text = dailyWeatherData.weatherType.weatherDesc,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.offset((-20).dp,0.dp)
                .width(80.dp), // Fixed width for chances of rain

            textAlign = TextAlign.Center
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorResource(id = R.color.dodger_blue))) { // Low Temp in Blue
                    append("${dailyWeatherData.lowTemperatures.roundToInt()}°")
                }
                append(" ") // Separator
                withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_red))) { // High Temp in Red
                    append("${dailyWeatherData.maxTemperatures.roundToInt()}°")
                }
            },
            fontSize = 16.sp,
            modifier = Modifier.offset((-10).dp, 0.dp)
                .width(80.dp),
            textAlign = TextAlign.Center
        )

    }
}@Preview(showBackground = true)
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
            //   .background(Color(0xFF121212))
            .fillMaxWidth(),
    )
}

