package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.utils.ThemeColors
import com.makaota.weathermzansi.weather.DailyWeatherData
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun DailyWeatherDisplay(
    index: Int,
    dailyWeatherData: DailyWeatherData,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {
    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

    val day = remember(dailyWeatherData) { dailyWeatherData.time.format(dayFormatter) }
    val date = remember(dailyWeatherData) { dailyWeatherData.time.format(dateFormatter) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .padding(10.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // **Date Section**
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$day, $date",
                color = textColor,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }

        // **Rain Chance & Weather Icon**
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${dailyWeatherData.chancesOfRain.roundToInt()}%",
                color = colorResource(id = R.color.dodger_blue),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = dailyWeatherData.weatherType.iconRes),
                contentDescription = null,
                modifier = Modifier.size(27.dp)
            )
        }

        // **Temperature Section**
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorResource(id = R.color.dodger_blue))) {
                    append("${dailyWeatherData.lowTemperatures.roundToInt()}°")
                }
                append(" ") // Separator
                withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_red))) {
                    append("${dailyWeatherData.maxTemperatures.roundToInt()}°")
                }
            },
            fontSize = 16.sp,
            modifier = Modifier
                .width(80.dp),
            textAlign = TextAlign.End
        )
    }
}

