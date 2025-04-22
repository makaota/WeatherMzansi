package com.makaota.weathermzansi.presentation

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.data.location_database.AppDatabase
import com.makaota.weathermzansi.data.location_database.LocationDao
import com.makaota.weathermzansi.data.location_database.LocationEntity
import com.makaota.weathermzansi.data.repository.LocationTrackerImpl
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppWithDrawer(
    navController: NavHostController = rememberNavController(),
    combinedViewModel: CombinedWeatherViewModel,
    locationDao: LocationDao,
    themeViewModel: ThemeViewModel,
    hourlyWeatherState: WeatherState

) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val locationTracker = remember { LocationTrackerImpl(context) }

    // **Observe Current Screen Route**
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route




    // Get User’s Current Location When Screen Loads
    LaunchedEffect(Unit) {
        val location = locationTracker.getCurrentLocation()
        if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            combinedViewModel.updateLocation("Loading...", latLng) // Update ViewModel

            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    ?.firstOrNull()
                combinedViewModel.updateLocation(address?.locality ?: "Unknown Location", latLng)
            } catch (e: Exception) {
                combinedViewModel.updateLocation("Unknown Location", latLng)
            }

            combinedViewModel.loadWeather(location.latitude, location.longitude)
        } else {
            combinedViewModel.updateLocation("Location Unavailable", null)
            combinedViewModel.setError("Could not fetch location. Please check permissions or GPS.")
            Log.e("WeatherApp", "Location is null")

        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } },
                themeViewModel = themeViewModel
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        val validRoutes = listOf("home", "cityManagement","settingsScreen", "WeatherDetails") // Define your valid routes
                        if (currentRoute != null && currentRoute in validRoutes) {
                            if (currentRoute == "home") {
                                // Show Menu Icon on Home Screen
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Menu"
                                    )
                                }
                            } else {
                                // Show Back Button on Other Screens
                                IconButton(onClick = { navController.navigateUp() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        }
                    },
                    title = {
                        if (currentRoute == "home") {
                            // Show Location Search Bar on Home Screen
                            LocationSearchBar { city, coordinates ->
                                combinedViewModel.updateLocation(city, coordinates)
                                combinedViewModel.loadWeather(
                                    coordinates.latitude,
                                    coordinates.longitude
                                )


                                // Ask User Before Saving the Location
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Save $city to history?",
                                        actionLabel = "Save",
                                        duration = SnackbarDuration.Long
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        val db = Room.databaseBuilder(
                                            context,
                                            AppDatabase::class.java,
                                            "weather_db"
                                        ).build()
                                        val locationDao1 = db.locationDao()
                                        locationDao1.insertLocation(
                                            LocationEntity(
                                                city,
                                                coordinates.latitude,
                                                coordinates.longitude
                                            )
                                        )
                                        snackbarHostState.showSnackbar(
                                            "$city saved to history!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }


                        }
                        else {
                            // Show Simple Title for Other Screens
                            if (currentRoute == "cityManagement"){
                                Text("City Management")
                            }
                            else if (currentRoute == "WeatherDetails"){
                                Text("7-Day forecast")
                            }
                            else if (currentRoute == "settingsScreen"){
                                Text("Settings")
                            }

                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding) // Ensures content doesn't overlap topBar
            ) {
                AppNavigation(navController, combinedViewModel,locationDao, themeViewModel = themeViewModel,
                    hourlyWeatherState =hourlyWeatherState )

                // **Snackbar Positioned at the Bottom**
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}
