package com.makaota.weathermzansi.domain.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.makaota.weathermzansi.R

object ThemeColors {
    @Composable
    fun textColor(): Color = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(id = R.color.dark_gray)

    @Composable
    fun backgroundColor(): Color = if (isSystemInDarkTheme()) Color(0xFF1E293B)
    else Color.LightGray.copy(alpha = 0.5f)

    @Composable
    fun labelColor(): Color = if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
    else colorResource(id = R.color.medium_gray)
}
