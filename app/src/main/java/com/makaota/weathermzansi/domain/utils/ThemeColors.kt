package com.makaota.weathermzansi.domain.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.makaota.weathermzansi.R

object ThemeColors {
    @Composable
    fun textColor(isDarkTheme: Boolean): Color = if (isDarkTheme) colorResource(id = R.color.white)
    else colorResource(id = R.color.dark_gray)

    @Composable
    fun backgroundColor(isDarkTheme: Boolean): Color = if (isDarkTheme) Color(0xFF1E293B)
    else Color.LightGray.copy(alpha = 0.5f)

    @Composable
    fun backgroundColor2(isDarkTheme: Boolean): Color = if (isDarkTheme) colorResource(id = R.color.black)
    else colorResource(id = R.color.white)

    @Composable
    fun labelColor(isDarkTheme: Boolean): Color = if (isDarkTheme) colorResource(id = R.color.light_steel_blue)
    else colorResource(id = R.color.medium_gray)





}

