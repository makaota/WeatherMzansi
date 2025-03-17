package com.makaota.weathermzansi.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun WindInfo(windSpeed: Double, windDegrees: Double, modifier: Modifier = Modifier) {
    val windDirection = getWindDirection(windDegrees)

    Row(
        modifier = modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.wind_speed),
            contentDescription = "Wind",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Wind: $windSpeed km/h $windDirection",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun getWindDirection(degrees: Double): String {
    val directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val index = ((degrees % 360) / 45).toInt()
    return directions[index]
}

@Composable
fun WindArrow(degrees: Float, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = R.drawable.wind_direction), // Use a custom arrow icon
        contentDescription = "Wind Direction",
        tint = Color.White,
        modifier = modifier
            .size(32.dp)
            .rotate(degrees)
    )
}

@Composable
fun WindDisplay(windSpeed: Double, windDegrees: Double) {

}

@Composable
fun WindDirectionCompass(
    windDegrees: Float, // Wind direction in degrees
    modifier: Modifier = Modifier,
    windSpeed: Double,

    ) {

    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )

    val labelColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
        else colorResource(id = R.color.medium_gray)

    val backgroundColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
        else colorResource(
            id = R.color.sky_blue
        )

    val backgroundColor2 =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_image_dark)
        else colorResource(
            id = R.color.day_image_dark_blue
        )


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
            modifier = modifier.height(150.dp) // Adjust size as needed
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
                    color = Color.Gray,
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
                            color = android.graphics.Color.BLACK
                            textSize = 30f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }


            AnimatedWindArrow(degrees = windDegrees)
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, bottom = 16.dp)
        ) {

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)) {
                        append("${windSpeed.roundToInt()}") // Larger wind speed
                    }
                    append(" km/h") // Regular text
                },
                color = textColor, // Change to your preferred color
                fontSize = 16.sp, // Default size for non-highlighted text
                textAlign = TextAlign.Start,
//                modifier = Modifier
//                    .align(Alignment.Start)
//                    .padding(16.dp)
            )
         //   Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = getWindDirection(windDegrees.toDouble()),
                color = textColor, // Change to your preferred color
                fontSize = 16.sp, // Default size for non-highlighted text
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .align(Alignment.Start)
//                    .padding(start = 16.dp)
            )

        }

    }
}

@Composable
fun AnimatedWindArrow(degrees: Float) {
    val animatedRotation by animateFloatAsState(
        targetValue = degrees,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "WindRotation"
    )

    Icon(
        painter = painterResource(id = R.drawable.wind_direction), // Custom arrow
        contentDescription = "Wind Direction",
        modifier = Modifier
            .size(40.dp)
            .rotate(animatedRotation) //  Rotates smoothly
    )
}

@Composable
fun PressureGauge(pressure: Float, modifier: Modifier) {
    val minPressure = 950f
    val maxPressure = 1050f

    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )

    val labelColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
        else colorResource(id = R.color.medium_gray)

    val backgroundColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
        else colorResource(
            id = R.color.sky_blue
        )

    val backgroundColor2 =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_image_dark)
        else colorResource(
            id = R.color.day_image_dark_blue
        )

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
            modifier = Modifier.size(150.dp) // Adjust size as needed
        ) {

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2.5f

                // Draw circular background
                drawCircle(color = Color.LightGray, radius = radius, center = center)

                // Draw colored pressure zones (Green = Low, Yellow = Normal, Red = High)
                drawArc(Color.Green, -120f, 80f, false, style = Stroke(20f))  // Low Pressure
                drawArc(Color.Yellow, -40f, 80f, false, style = Stroke(20f))  // Normal Pressure
                drawArc(Color.Red, 40f, 80f, false, style = Stroke(20f))  // High Pressure

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
                .padding(start = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.Start,

            ) {

            // Display Pressure Value
            Text(
                text = "${pressure.roundToInt()} hPa",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                textAlign = TextAlign.Start,
//                modifier = Modifier
//                    .align(Alignment.Start)
//                    .padding(16.dp)
            )

           // Spacer(modifier = Modifier.height(8.dp))

            when (pressureColor) {
                Color.Green -> {

                    LegendItem("Low", Color.Green)

                }

                Color.Yellow -> {

                    LegendItem("Normal", Color.Yellow)

                }

                else -> {
                    LegendItem("High", Color.Red)
                }
            }
        }


    }
}

// **Reusable Color Legend Item**
@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPressureGauge() {
    PressureGauge(pressure = 1015f, modifier = Modifier) // Example Pressure
}

@Composable
fun HumidityGauge(humidity: Float) {
    val minHumidity = 0f
    val maxHumidity = 100f

    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )

    val labelColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
        else colorResource(id = R.color.medium_gray)

    val backgroundColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
        else colorResource(
            id = R.color.sky_blue
        )

    val backgroundColor2 =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_image_dark)
        else colorResource(
            id = R.color.day_image_dark_blue
        )


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
            modifier = Modifier.size(150.dp) // Adjust size as needed
        ) {
            Canvas(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2.5f

                // Draw circular background
                drawCircle(color = Color.LightGray, radius = radius, center = center)

                // Draw colored humidity zones
                drawArc(Color.Blue, -120f, 80f, false, style = Stroke(20f)) // Low Humidity
                drawArc(Color.Green, -40f, 80f, false, style = Stroke(20f)) // Comfortable
                drawArc(Color(0xFFFFA500), 40f, 40f, false, style = Stroke(20f)) // High Humidity
                drawArc(Color.Red, 75f, 45f, false, style = Stroke(20f)) // Very High Humidity

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
                .padding(start = 16.dp, bottom = 16.dp),
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

                    LegendItem("Low", Color.Blue)

                }

                Color.Green -> {
                    LegendItem("Comfortable", Color.Green)
                }

                Color(0xFFFFA500) -> {
                    LegendItem("High", colorResource(id = R.color.orange))
                }

                else -> {
                    LegendItem("Very High", Color.Red)
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewHumidityGauge() {
    HumidityGauge(humidity = 105f) // Example Humidity
}

