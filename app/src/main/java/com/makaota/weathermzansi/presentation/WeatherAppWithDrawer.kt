package com.makaota.weathermzansi.presentation

import android.location.Geocoder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.data.location_database.AppDatabase
import com.makaota.weathermzansi.data.location_database.LocationEntity
import com.makaota.weathermzansi.data.repository.LocationTrackerImpl
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppWithDrawer(
    navController: NavHostController = rememberNavController(),
    combinedViewModel: CombinedWeatherViewModel,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var latLng by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val db = Room.databaseBuilder(context, AppDatabase::class.java, "weather_db").build()
    val locationDao = db.locationDao()
    val locationTracker = remember { LocationTrackerImpl(context) }




    // Get User’s Current Location When Screen Loads
    LaunchedEffect(Unit) {
        val location = locationTracker.getCurrentLocation()
        if (location != null) {
            latLng = LatLng(location.latitude, location.longitude)
            combinedViewModel.fetchWeather(location.latitude, location.longitude)

            // Reverse Geocode to Get City Name
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    ?.firstOrNull()
                selectedLocation = address?.locality ?: "Unknown Location"
            } catch (e: Exception) {
                selectedLocation = "Unknown Location"
            }
        } else {
            selectedLocation = "Location Unavailable"
        }
        val locations = locationDao.getAllLocations()

    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController) { scope.launch { drawerState.close() } }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    title = {
                        LocationSearchBar { city, coordinates ->
                            selectedLocation = city
                            latLng = coordinates
                            combinedViewModel.fetchWeather(
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
                                    locationDao.insertLocation(
                                        LocationEntity(
                                            city,
                                            coordinates.latitude,
                                            coordinates.longitude
                                        )
                                    )
                                    snackbarHostState.showSnackbar(
                                        message = "$city saved to history!",
                                        duration = SnackbarDuration.Short
                                    )
                                }
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
                AppNavigation(navController, combinedViewModel)

                // **Snackbar Positioned at the Bottom**
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp) // Adds padding to avoid edge overlap
                )
            }
        }
    }

}
