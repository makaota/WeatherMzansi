package com.makaota.weathermzansi.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController, combinedViewModel: CombinedWeatherViewModel) {
    NavHost(navController, startDestination = "home") {
        composable("home") { WeatherApp(combinedViewModel) }
        composable("settings") { SettingsScreen() }
        composable("about") { AboutScreen() }
    }
}
