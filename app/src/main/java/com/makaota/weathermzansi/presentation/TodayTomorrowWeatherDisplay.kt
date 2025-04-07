package com.makaota.weathermzansi.presentation


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.utils.ThemeColors
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun TodayTomorrowWeatherDisplay(
    dailyState: DailyWeatherState,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)






    dailyState.dailyWeatherInfo?.dailyWeatherData?.let { data ->

        // Get the current time
        val currentTime = remember { LocalDateTime.now() }
        val formattedCurrentTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"))

        // Extract the first day's data for highs, lows, and rain chance
        val todayWeatherData = dailyState.dailyWeatherInfo.dailyWeatherData.get(0)
        val tomorrowWeatherData = dailyState.dailyWeatherInfo.dailyWeatherData.get(1)

        Log.d("TodayTomorrow", "TodayWeatherData: $todayWeatherData")

        Log.d("TodayTomorrow", "TomorrowWeatherData: $tomorrowWeatherData")

        Log.d("TodayTomorrow", "DailyWeatherData: $data")

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight() // Ensure the Row does not fill the screen height
                .padding(start = 16.dp, end = 16.dp)
                .clip(RoundedCornerShape(10.dp))
              //  .background(backgroundColor)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp) // Inner padding
                ) {
                    todayWeatherData?.let { todayData ->
                        Column (modifier = Modifier.padding(8.dp)){
                            Text(
                                text = "Today",
                                color = textColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_red))) { // High Temp in Red
                                        append("${todayData.get(0).maxTemperatures.roundToInt()}°")
                                    }
                                    append(" ") // Separator
                                    withStyle(style = SpanStyle(color = colorResource(id = R.color.dodger_blue))) { // Low Temp in Blue
                                        append("${todayData.get(0).lowTemperatures.roundToInt()}°")
                                    }

                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = todayData.get(0).weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)

                        )
                    }
                }
            }

        Spacer(modifier = Modifier.width(8.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                modifier = Modifier
                    .weight(1f)

            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    tomorrowWeatherData?.let { tomorrowData ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(
                                text = "Tomorrow",
                                color = textColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_red))) { // High Temp in Red
                                        append("${tomorrowData.get(0).maxTemperatures.roundToInt()}°")
                                    }
                                    append(" ") // Separator
                                    withStyle(style = SpanStyle(color = colorResource(id = R.color.dodger_blue))) { // Low Temp in Blue
                                        append("${tomorrowData.get(0).lowTemperatures.roundToInt()}°")
                                    }

                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                       // Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = tomorrowData.get(0).weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)

                        )

                    }
                }
            }
        }
    }

}









