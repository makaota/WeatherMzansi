package com.makaota.weathermzansi.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.utils.ThemeColors
import com.makaota.weathermzansi.ui.theme.SkyBlue
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


fun getWindDirection(degrees: Double): String {
    val directions = arrayOf("North", "North East", "East", "South East", "South", "South West", "West", "North West")
    val index = ((degrees % 360) / 45).toInt()
    return directions[index]
}

@Composable
fun WindDirectionCompass(
    windDegrees: Float, // Wind direction in degrees
    modifier: Modifier = Modifier,
    windSpeed: Double,
    themeViewModel: ThemeViewModel

    ) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)

    val compassTextColor = if (isDarkTheme) {
        Color.White.toArgb()
    } else {
        Color.Black.toArgb()
    }




    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Wind",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 16.dp)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.height(130.dp) // Adjust size as needed
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2.5f // Adjust circle size

                // Draw Circle
                drawCircle(
                    color = textColor,
                    style = Stroke(width = 5f)
                )

                // Draw Direction Labels
                val directions = listOf("N" to -90f, "E" to 0f, "S" to 90f, "W" to 180f)
                for ((label, angle) in directions) {
                    val angleRad = Math.toRadians(angle.toDouble())
                    val textOffset = Offset(
                        (center.x + radius * cos(angleRad)).toFloat() - 10f, // Adjust text centering
                        (center.y + radius * sin(angleRad)).toFloat() + 5f
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        textOffset.x,
                        textOffset.y,
                        android.graphics.Paint().apply {
                            color = compassTextColor
                            textSize = 40f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                        }
                    )
                }
            }


            AnimatedWindArrow(degrees = windDegrees, textColor)
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp)
        ) {

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) {
                        append("${windSpeed.roundToInt()}") // Larger wind speed
                    }
                    append(" km/h") // Regular text
                },
                color = textColor, // Change to your preferred color
                fontSize = 16.sp, // Default size for non-highlighted text
                textAlign = TextAlign.Start,
            )
         //   Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = getWindDirection(windDegrees.toDouble()),
                color = textColor, // Change to your preferred color
                fontSize = 14.sp, // Default size for non-highlighted text
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
            )

        }

    }
}


@Composable
fun AnimatedWindArrow(degrees: Float, textColor: Color) {
    val animatedRotation by animateFloatAsState(
        targetValue = degrees,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "WindRotation"
    )

    Icon(
        painter = painterResource(id = R.drawable.wind_direction), // Custom arrow
        contentDescription = "Wind Direction",
        tint = textColor,
        modifier = Modifier
            .size(26.dp)
            .rotate(animatedRotation) //  Rotates smoothly
    )
}

@Composable
fun PressureGauge(pressure: Float,themeViewModel: ThemeViewModel) {
    val minPressure = 950f
    val maxPressure = 1050f

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)


    // Normalize pressure to an angle between -120° (Low) and 120° (High)
    val needleAngle = ((pressure - minPressure) / (maxPressure - minPressure) * 240f) - 120f

    // Smoothly animate the needle movement
    val animatedAngle by animateFloatAsState(targetValue = needleAngle, animationSpec = tween(1000))

    // Determine color based on pressure range (Standard: Green = Low, Yellow = Normal, Red = High)
    val pressureColor = when {
        pressure < 1000 -> Color.Green  // Low Pressure
        pressure in 1000f..1020f -> Color.Yellow // Normal Pressure
        else -> Color.Red  // High Pressure
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = "Pressure",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 16.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(130.dp) // Adjust size as needed
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2.5f

                // Draw circular background
                drawCircle(color = SkyBlue.copy(alpha = 0.5f), radius = radius, center = center)

                // Draw colored pressure zones (Green = Low, Yellow = Normal, Red = High)
                drawArc(Color.Green.copy(alpha = 0.5f), -125f, 95f, false, style = Stroke(20f))  // Low Pressure
                drawArc(Color.Yellow.copy(alpha = 0.5f), -30f, 80f, false, style = Stroke(20f))  // Normal Pressure
                drawArc(Color.Red.copy(alpha = 0.5f), 50f, 80f, false, style = Stroke(20f))  // High Pressure

                // Draw Needle (Arrow)
                val needleEnd = Offset(
                    x = center.x + radius * cos(Math.toRadians(animatedAngle.toDouble())).toFloat(),
                    y = center.y + radius * sin(Math.toRadians(animatedAngle.toDouble())).toFloat()
                )

                drawLine(
                    color = Color.Black,
                    start = center,
                    end = needleEnd,
                    strokeWidth = 4f
                )

                // Draw Center Circle
                drawCircle(color = Color.Black, radius = 10f, center = center)
            }
        }

        // **Color Legend**
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start,

            ) {

            // Display Pressure Value
            Text(
                text = "${pressure.roundToInt()} hPa",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Start,
            )

           // Spacer(modifier = Modifier.height(8.dp))


            when (pressureColor) {
                Color.Green -> {

                    LegendItem("Low", Color.Green.copy(alpha = 0.5f), textColor)

                }

                Color.Yellow -> {

                    LegendItem("Normal", Color.Yellow.copy(alpha = 0.5f), textColor)

                }

                else -> {
                    LegendItem("High", Color.Red.copy(alpha = 0.5f), textColor)
                }
            }
        }


    }
}

// **Reusable Color Legend Item**
@Composable
fun LegendItem(label: String, color: Color, textColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 16.dp)
            .fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewPressureGauge() {
//
//    PressureGauge(pressure = 1013f, themeViewModel = ) // Example Pressure
//}

@Composable
fun HumidityGauge(humidity: Float,themeViewModel: ThemeViewModel) {
    val minHumidity = 0f
    val maxHumidity = 100f

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)



    // Normalize humidity to an angle between -120° (Low) and 120° (High)
    val needleAngle = ((humidity - minHumidity) / (maxHumidity - minHumidity) * 240f) - 120f

    // Animate needle movement
    val animatedAngle by animateFloatAsState(targetValue = needleAngle, animationSpec = tween(1000))

    // Determine color based on humidity level
    val humidityColor = when {
        humidity < 30 -> Color.Blue  // Low Humidity
        humidity in 30f..60f -> Color.Green  // Comfortable
        humidity in 60f..80f -> Color(0xFFFFA500) // Orange - High Humidity
        else -> Color.Red  // Very High Humidity
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Humidity",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 16.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(130.dp) // Adjust size as needed
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2.5f

                // Draw circular background
                drawCircle(color = SkyBlue.copy(alpha = 0.5f), radius = radius, center = center)

                // Draw colored humidity zones
                drawArc(Color.Blue.copy(alpha = 0.5f), -131f, 80f, false, style = Stroke(20f)) // Low Humidity
                drawArc(Color.Green.copy(alpha = 0.5f), -52f, 78f, false, style = Stroke(20f)) // Comfortable
                drawArc(Color(0xFFFFA500).copy(alpha = 0.5f), 26f, 48f, false, style = Stroke(20f)) // High Humidity
                drawArc(Color.Red.copy(alpha = 0.5f), 74f, 68f, false, style = Stroke(20f)) // Very High Humidity

                // Draw Needle (Arrow)
                val needleEnd = Offset(
                    x = center.x + radius * cos(Math.toRadians(animatedAngle.toDouble())).toFloat(),
                    y = center.y + radius * sin(Math.toRadians(animatedAngle.toDouble())).toFloat()
                )

                drawLine(
                    color = Color.Black,
                    start = center,
                    end = needleEnd,
                    strokeWidth = 4f
                )

                // Draw Center Circle
                drawCircle(color = Color.Black, radius = 10f, center = center)
            }
        }

        // **Color Legend**
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start,

            ){

            // Display Humidity Value
            Text(
                text = "${humidity.roundToInt()}%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Start,
//                modifier = Modifier.align(Alignment.Start)
//                    .padding(16.dp)
            )

            when (humidityColor) {
                Color.Blue -> {

                    LegendItem("Low", Color.Blue.copy(alpha = 0.5f), textColor)

                }

                Color.Green -> {
                    LegendItem("Comfortable", Color.Green.copy(alpha = 0.5f), textColor)
                }

                Color(0xFFFFA500) -> {
                    LegendItem("High", colorResource(id = R.color.orange).copy(alpha = 0.5f), textColor)
                }

                else -> {
                    LegendItem("Very High", Color.Red.copy(alpha = 0.5f), textColor)
                }
            }
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun PreviewHumidityGauge() {
//    HumidityGauge(humidity = 81f) // Example Humidity
//}
//
