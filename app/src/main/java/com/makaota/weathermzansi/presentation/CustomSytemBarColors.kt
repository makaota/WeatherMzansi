package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CustomSystemBarColors() {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = if (isSystemInDarkTheme()) Color(0xFF1E3A5F) else Color(0xFF87CEEB) // Replace with your desired color

    systemUiController.setStatusBarColor(
        color = statusBarColor,
        darkIcons = true // Adjust icons to be light/dark depending on your background color
    )

}