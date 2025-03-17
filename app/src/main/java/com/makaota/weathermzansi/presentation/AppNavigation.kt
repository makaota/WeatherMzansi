package com.makaota.weathermzansi.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.makaota.weathermzansi.data.location_database.LocationDao

@Composable
fun AppNavigation(navController: NavHostController, combinedViewModel: CombinedWeatherViewModel,
                  locationDao: LocationDao) {
    NavHost(navController, startDestination = "home") {
        composable("home") { WeatherApp(combinedViewModel, navController) }
        composable("cityManagement") { CityManagementScreen(combinedViewModel,
            navController,
            locationDao) }
        composable("about") { AboutScreen() }
        composable("WeatherDetails") { WeatherDetailsScreen(combinedViewModel, navController) }

    }
}
