package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DayNightProgressIndicator(
    currentTimeProgress: Float, // Progress of the current time (0.0 to 1.0)
    sunriseProgress: Float,    // Progress of sunrise (0.0 to 1.0)
    sunsetProgress: Float,     // Progress of sunset (0.0 to 1.0)
    modifier: Modifier = Modifier,
    dayColor: Color = Color.Yellow,
    nightColor: Color = Color.DarkGray,
    sunColor: Color = Color(0xFFFFA500),
    moonColor: Color = Color(0xFF87CEEB),
    circleThickness: Dp = 8.dp
) {
    Canvas(
        modifier = modifier
            .size(250.dp) // Circle size
            .padding(16.dp)
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = (size.minDimension - circleThickness.toPx()) / 2

        // Daytime arc (sunrise to sunset)
        val daySweepAngle = (sunsetProgress - sunriseProgress) * 360f
        val dayStartAngle = sunriseProgress * 360f - 90f
        drawArc(
            color = dayColor,
            startAngle = dayStartAngle,
            sweepAngle = daySweepAngle,
            useCenter = false,
            style = Stroke(width = circleThickness.toPx(), cap = StrokeCap.Round)
        )

        // Nighttime arc (sunset to sunrise)
        val nightSweepAngle = 360f - daySweepAngle
        val nightStartAngle = dayStartAngle + daySweepAngle
        drawArc(
            color = nightColor,
            startAngle = nightStartAngle,
            sweepAngle = nightSweepAngle,
            useCenter = false,
            style = Stroke(width = circleThickness.toPx(), cap = StrokeCap.Round)
        )

        // Sun position
        if (currentTimeProgress in sunriseProgress..sunsetProgress) {
            val sunAngle = 360 * currentTimeProgress - 90f
            val sunX = center.x + radius * cos(Math.toRadians(sunAngle.toDouble())).toFloat()
            val sunY = center.y + radius * sin(Math.toRadians(sunAngle.toDouble())).toFloat()
            drawCircle(
                color = sunColor,
                radius = 12.dp.toPx(),
                center = Offset(sunX, sunY)
            )
        }

        // Moon position
        if (currentTimeProgress !in sunriseProgress..sunsetProgress) {
            val moonAngle = 360 * currentTimeProgress - 90f
            val moonX = center.x + radius * cos(Math.toRadians(moonAngle.toDouble())).toFloat()
            val moonY = center.y + radius * sin(Math.toRadians(moonAngle.toDouble())).toFloat()
            drawCircle(
                color = moonColor,
                radius = 12.dp.toPx(),
                center = Offset(moonX, moonY)
            )
        }
    }
}


@Composable
fun DayNightProgressExample() {
    var currentTimeProgress by remember { mutableStateOf(0.5f) } // Default to midday (50%)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        DayNightProgressIndicator(
            currentTimeProgress = currentTimeProgress,
            sunriseProgress = 0.25f, // Sunrise at 25% (6:00 AM)
            sunsetProgress = 0.75f,  // Sunset at 75% (6:00 PM)
            dayColor = Color.Yellow,
            nightColor = Color.DarkGray,
            sunColor = Color(0xFFFFA500),
            moonColor = Color(0xFF87CEEB),
            circleThickness = 10.dp,
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = currentTimeProgress,
            onValueChange = { currentTimeProgress = it },
            valueRange = 0f..1f,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}
