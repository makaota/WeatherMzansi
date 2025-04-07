package com.makaota.weathermzansi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.makaota.weathermzansi.data.theme_datastore.AppColors

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


//object DarkThemeColors {
//    val background = Color(0xFF1E293B)
//    val text = Color.White
//    val label = Color.LightGray
//}
//
//object LightThemeColors {
//    val background = Color.LightGray.copy(alpha = 0.5f)
//    val text = Color.DarkGray
//    val label = Color.LightGray
//}

val LightThemeColors = AppColors(
    background = Color.LightGray.copy(alpha = 0.5f),
    text = Color.DarkGray,
    label = Color.LightGray
)

val DarkThemeColors = AppColors(
    background = Color(0xFF1E293B), // Dark blue-gray
    text = Color.White,
    label = Color(0xFFB0C4DE) // Light Steel Blue
)


val LocalBackground = staticCompositionLocalOf { Color.White }
val LocalText = staticCompositionLocalOf { Color.Black }
val LocalLabel = staticCompositionLocalOf { Color.Gray }

@Composable
fun WeatherMzansiTheme(darkTheme: Boolean = isSystemInDarkTheme(), // Automatically detects system theme
                       content: @Composable () -> Unit) {

    val colors = if (darkTheme) DarkThemeColors else LightThemeColors


    CompositionLocalProvider(
        LocalBackground provides colors.background, // ✅ Ensure proper color assignment
        LocalText provides colors.text,
        LocalLabel provides colors.label
    ) {
        content()
    }
}


//@Composable
//fun WeatherMzansiTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
//    content: @Composable () -> Unit
//) {
//    val textColor = ThemeColors.textColor()
//    val backgroundColor = ThemeColors.backgroundColor()
//    val labelColor = ThemeColors.labelColor()
//
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//
//    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography,
//        content = content
//    )
//}