package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.makaota.weathermzansi.domain.utils.ThemeColors
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DaylightDurationLayout(
    sunriseTime: LocalTime,
    sunsetTime: LocalTime,
    daylightDuration: Duration, // Use daylight duration!
  //  currentTime: LocalTime = LocalTime.now(),
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel
) {
    val barBackgroundColor = if (isSystemInDarkTheme()) Color.Gray else Color.Gray.copy(alpha = 0.3f) // Light Gray
    val progressColor = Color(0xFFFFD700) // Golden Yellow
    val sunColor = Color(0xFFFFA500) // Sun icon

    val currentTime = rememberUpdatedState(LocalTime.now())


    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    val totalDayMinutes = daylightDuration.toMinutes().toFloat()

    // Ensure correct time difference based on daylight
    val elapsedMinutes = when {
        currentTime.value.isBefore(sunriseTime) -> 0f // Before sunrise, progress is 0
        currentTime.value.isAfter(sunsetTime) -> totalDayMinutes // After sunset, progress stays full
        else -> Duration.between(sunriseTime, currentTime.value).toMinutes().toFloat()
    }

    val sunProgress = (elapsedMinutes / totalDayMinutes).coerceIn(0f, 1f) // Clamp between 0 and 1

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor, shape = RoundedCornerShape(12.dp)),
      //  horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f), // Ensures semicircle shape
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val centerX = size.width / 2
                val centerY = size.height
                val radius = size.width / 2.5f

                // **Draw Full Daylight Semicircle (Background)**
                val daylightPath = Path().apply {
                    moveTo(centerX - radius, centerY)
                    quadraticTo(
                        centerX, centerY - radius * 2,
                        centerX + radius, centerY
                    )
                    lineTo(centerX, centerY)
                    close()
                }
                drawPath(daylightPath, barBackgroundColor)

                // **Draw Filled Semicircle Based on Progress**
                val progressAngle = sunProgress * 180f
                val radians = Math.toRadians(progressAngle.toDouble())
                val progressX = (centerX - radius * cos(radians)).toFloat() // **Fixed direction**
                val progressY = (centerY - radius * sin(radians)).toFloat()

                val progressPath = Path().apply {
                    moveTo(centerX - radius, centerY)
                    quadraticTo(
                        centerX, centerY - radius * 2,
                        progressX, progressY
                    )
                    lineTo(centerX, centerY)
                    close()
                }
                drawPath(progressPath, progressColor)

                // **Sun Position Calculation**
                drawCircle(
                    color = sunColor,
                    radius = 14.dp.toPx(),
                    center = Offset(progressX, progressY)
                )
            }
        }

      //  Spacer(modifier = Modifier.height(16.dp))

        // **Sunrise, Noon & Sunset Values**
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "🌅 ${sunriseTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}", color = textColor)
            Text(text = "☀️ Noon", color = textColor)
            Text(text = "🌙 ${sunsetTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}", color = textColor)
        }
    }
}

//@Preview(name = "Light Mode", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
//@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
//@Composable
//fun PreviewDaylightDurationLayout() {
//    DaylightDurationLayout(
//        sunriseTime = LocalTime.of(6, 45),  // 6:30 AM
//        sunsetTime = LocalTime.of(18, 45),  // 6:45 PM
//        daylightDuration = Duration.ofHours(12), // Example: 12-hour daylight
//    )
//}

