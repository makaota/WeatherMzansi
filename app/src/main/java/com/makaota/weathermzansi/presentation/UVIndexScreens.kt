package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.ui.theme.SkyBlue
import kotlin.math.roundToInt

@Composable
fun UVIndexGradient(uvIndex: Float) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.Green, Color.Yellow, Color(0xFFFFA500), Color.Red, Color(0xFF8B00FF))
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(SkyBlue, shape = RoundedCornerShape(12.dp))
            .padding(start = 16.dp, end = 16.dp)
    ) {

        UVIndexSunIcon(uvIndex = uvIndex)


        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(gradientBrush, shape = RoundedCornerShape(12.dp))
        ) {
            Box(
                modifier = Modifier
                    .offset(
                        x = (uvIndex / 11f * 300.dp).coerceIn(0.dp, 300.dp)
                    )
                    .size(16.dp)
                    .background(Color.Black, shape = CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        UVLegend()
    }
}

@Composable
fun UVIndexSunIcon(uvIndex: Float) {


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


    val uvColor = when {
        uvIndex <= 2 -> Color.Green // Low
        uvIndex <= 5 -> Color.Yellow // Moderate
        uvIndex <= 7 -> Color(0xFFFFA500) // High (Orange)
        uvIndex <= 10 -> Color.Red // Very High
        else -> Color(0xFF8B00FF) // Extreme (Purple)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_sunny),
            contentDescription = "UV Index",
            tint = uvColor,
            modifier = Modifier.size(50.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "UV Index: $uvIndex",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

    }
}

@Composable
fun UVLegend() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Low", color = Color.Green, fontSize = 14.sp)
        Text("Moderate", color = Color.Yellow, fontSize = 14.sp)
        Text("High", color = Color(0xFFFFA500), fontSize = 14.sp)
        Text("Very High", color = Color.Red, fontSize = 14.sp)
        Text("Extreme", color = Color(0xFF8B00FF), fontSize = 14.sp)
    }
}

@Composable
fun VisibilityBlurEffect(visibilityKm: Float, dewPoint: Double) {
    val blurRadius = (50f - visibilityKm).coerceIn(0f, 50f) // More blur for lower visibility
    val iconOpacity = (visibilityKm / 50f).coerceIn(0.2f, 1f) // Less opacity when visibility is low

    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(id = R.color.dark_gray)

    // Determine color based on visibility range
    val visibilityColor = when {
        visibilityKm < 1 -> Color.Red // Very Poor
        visibilityKm < 4 -> Color(0xFFFFA500) // Orange - Poor
        visibilityKm < 10 -> Color.Yellow // Moderate
        visibilityKm < 20 -> Color(0xFF90EE90) // Light Green - Good
        else -> Color.Green // Excellent
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
          //  .padding(16.dp)
    ) {

        Text(
            text = "Visibility",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        )

        Box(
            modifier = Modifier
                .size(130.dp)
                .blur(blurRadius.dp) // Apply dynamic blur
                .background(Color.Gray.copy(alpha = 0.3f))
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                //  .padding(16.dp)
            ) {


                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.visibility),
                    contentDescription = "Visibility Icon",
                    modifier = Modifier.size(100.dp),
                    tint = visibilityColor.copy(alpha = iconOpacity) // Color changes with visibility
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val visibilityText = when {
            visibilityKm >= 20000 -> "Unlimited"
            visibilityKm >= 1000 -> String.format("%.1fk km", visibilityKm / 1000) // Converts to "16.4k km"
            else -> "${visibilityKm.roundToInt()} km"
        }
        // Display Visibility Value
        Text(
            text = "$visibilityText",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )

        // Visibility Scale Legend
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            when(visibilityColor){
                Color.Red ->{
                    LegendItems("Very Poor (<1 km)", Color.Red)
                }
                Color(0xFFFFA500) ->{
                    LegendItems("Poor (1 - 4 km)", Color(0xFFFFA500))
                }
                Color.Yellow ->{
                    LegendItems("Moderate (4 - 10 km)", Color.Yellow)
                }
                Color(0xFF90EE90)->{
                    LegendItems("Good (10 - 20 km)", Color(0xFF90EE90))
                }
                Color.Green ->{
                    LegendItems("Excellent (20+ km)", Color.Green)
                }

            }

        }
    }
}

@Composable
fun LegendItems(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
       // modifier = Modifier.padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 14.sp, color = color)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVisibilityBlur() {
    VisibilityBlurEffect(visibilityKm = 50000f, dewPoint = 10.0)
}

//@Composable
//fun VisibilityBlurEffect(visibilityKm: Float, dewPoint: Double) {
//    val blurRadius = (50f - visibilityKm).coerceIn(0f, 50f) // More blur for lower visibility
//    val iconOpacity = (visibilityKm / 50f).coerceIn(0.2f, 1f) // Less opacity when visibility is low
//
//
//    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
//    else colorResource(
//        id = R.color.dark_gray
//    )
//
//    val labelColor =
//        if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
//        else colorResource(id = R.color.medium_gray)
//
//    val backgroundColor =
//        if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
//        else colorResource(
//            id = R.color.sky_blue
//        )
//
//    val backgroundColor2 =
//        if (isSystemInDarkTheme()) colorResource(id = R.color.night_image_dark)
//        else colorResource(
//            id = R.color.day_image_dark_blue
//        )
//
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.fillMaxSize()
//        // .padding(16.dp)
//    ) {
//        Text(
//            text = "Visibility",
//            fontSize = 18.sp,
//            fontWeight = FontWeight.Bold,
//            color = textColor,
//            textAlign = TextAlign.Start,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(start = 16.dp, top = 16.dp)
//        )
//
//        Box(
//            modifier = Modifier
//                .size(150.dp)
//                .blur(blurRadius.dp) // Apply blur dynamically
//                .padding(16.dp)
//                .background(Color.Gray.copy(alpha = 0.3f)) // Slight gray tint for fog effect
//
//        ) {
//            Column(
//                modifier = Modifier.align(Alignment.Center),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Icon(
//                    imageVector = ImageVector.vectorResource(id = R.drawable.visibility), // 👁 Eye Icon
//                    contentDescription = "Visibility Icon",
//                    modifier = Modifier.size(60.dp),
//                    tint = Color.White.copy(alpha = iconOpacity) // Adjust opacity based on visibility
//                )
//
//
//
//
//            }
//        }
//        Text(
//            text = "${visibilityKm.toDouble()} km",
//            fontSize = 17.sp,
//            color = textColor,
//            textAlign = TextAlign.Start,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(start = 16.dp)
//        )
//
//        //   Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "Dew point: ${dewPoint.roundToInt()}°",
//            fontSize = 16.sp,
//            color = textColor,
//            textAlign = TextAlign.Start,
//            modifier = Modifier
//                .align(Alignment.Start)
//                .padding(start = 16.dp)
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewVisibilityBlur1() {
//    VisibilityBlurEffect(visibilityKm = 50f, dewPoint = 10.0) // Example: 10 km visibility
//}
