package com.makaota.weathermzansi.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.makaota.weathermzansi.data.location_database.LocationDao

@Composable
fun AppNavigation(navController: NavHostController, combinedViewModel: CombinedWeatherViewModel,
                  locationDao: LocationDao,themeViewModel: ThemeViewModel, hourlyWeatherState: WeatherState) {
    NavHost(navController, startDestination = "home") {
     //   composable("splash_screen") {SplashScreen(navController = navController, themeViewModel = themeViewModel) }
        composable("home") { WeatherApp(combinedViewModel, navController, themeViewModel) }
        composable("cityManagement") { CityManagementScreen(combinedViewModel,
            navController,
            locationDao,
            themeViewModel = themeViewModel) }
        composable("settingsScreen") { SettingsScreen(themeViewModel = themeViewModel) }
        composable("WeatherDetails") { WeatherDetailsScreen(combinedViewModel,
            navController,
            themeViewModel,
            hourlyWeatherState) }

    }
}
