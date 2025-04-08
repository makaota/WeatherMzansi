package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.utils.ThemeColors

@Composable
fun UVIndexGradient(uvIndex: Float,themeViewModel: ThemeViewModel) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color.Green, Color.Yellow, Color(0xFFFFA500), Color.Red, Color(0xFF8B00FF)),
    )

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(start = 16.dp, end = 16.dp)
    ) {

        UVIndexSunIcon(uvIndex = uvIndex, themeViewModel = themeViewModel)


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
fun UVIndexSunIcon(uvIndex: Float, themeViewModel: ThemeViewModel) {


    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)


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
        modifier = Modifier.fillMaxWidth()
            .background(colorResource(id = R.color.dark_gray))
        ,
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
fun VisibilityBlurEffect(visibilityKm: Float, themeViewModel: ThemeViewModel) {
    val blurRadius = (50f - visibilityKm).coerceIn(0f, 50f) // More blur for lower visibility
    val iconOpacity = (visibilityKm / 50f).coerceIn(0.2f, 1f) // Less opacity when visibility is low

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    // Determine color based on visibility range
    val visibilityColor = when {
        visibilityKm < 1000 -> Color.Red // Very Poor
        visibilityKm < 4000 -> Color(0xFFFFA500) // Orange - Poor
        visibilityKm < 10000 -> Color.Yellow // Moderate
        visibilityKm < 20000 -> Color(0xFF90EE90) // Light Green - Good
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
                .size(100.dp)
                .blur(blurRadius.dp) // Apply dynamic blur
                .background(Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(10.dp))
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
            visibilityKm >= 10000 -> "Unlimited"
            visibilityKm >= 1000 -> String.format("%.1f km", visibilityKm / 1000) // Converts to "16.4k km"
            else -> "${visibilityKm.toDouble()} km"
        }
        // Display Visibility Value
        Text(
            text = visibilityText,
            fontSize = 18.sp,
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
                    LegendItems("Very Poor (<1 km)", Color.Red, textColor)
                }
                Color(0xFFFFA500) ->{
                    LegendItems("Poor (1 - 4 km)", Color(0xFFFFA500), textColor)
                }
                Color.Yellow ->{
                    LegendItems("Moderate (4 - 10 km)", Color.Yellow, textColor)
                }
                Color(0xFF90EE90)->{
                    LegendItems("Good (10 - 20 km)", Color(0xFF90EE90), textColor)
                }
                Color.Green ->{
                    LegendItems("Excellent (20+ km)", Color.Green, textColor)
                }

            }

        }
    }
}

@Composable
fun LegendItems(label: String, color: Color, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 16.dp)
            .fillMaxWidth()){
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewVisibilityBlur() {
//    VisibilityBlurEffect(visibilityKm = 5400f)
//}



