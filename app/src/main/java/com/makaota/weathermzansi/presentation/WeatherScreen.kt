package com.makaota.weathermzansi.presentation

import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.data.location_database.LocationDao
import com.makaota.weathermzansi.data.location_database.LocationEntity
import com.makaota.weathermzansi.domain.location.LocationTracker
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun WeatherScreen(
    combinedWeatherViewModel: CombinedWeatherViewModel,
    locationTracker: LocationTracker,
    locationDao: LocationDao
) {
    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var latLng by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 🔥 Get User’s Current Location When Screen Loads
    LaunchedEffect(Unit) {
        val location = locationTracker.getCurrentLocation()
        if (location != null) {
            latLng = LatLng(location.latitude, location.longitude)
            combinedWeatherViewModel.fetchWeather(location.latitude, location.longitude)

            // ✅ Reverse Geocode to Get City Name
            val geocoder = Geocoder(context, Locale.getDefault())
            try {
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)?.firstOrNull()
                selectedLocation = address?.locality ?: "Unknown Location"
            } catch (e: Exception) {
                selectedLocation = "Unknown Location"
            }
        } else {
            selectedLocation = "Location Unavailable"
        }
        val locations = locationDao.getAllLocations()
        Log.d("DatabaseCheck", "Locations: $locations")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            // 🔥 Search Bar
            LocationSearchBar { city, coordinates ->
                selectedLocation = city
                latLng = coordinates
                combinedWeatherViewModel.fetchWeather(coordinates.latitude, coordinates.longitude)

                // ✅ Ask User Before Saving the Location
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Save $city to history?",
                        actionLabel = "Save",
                        duration = SnackbarDuration.Long
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        locationDao.insertLocation(LocationEntity(city, coordinates.latitude, coordinates.longitude))
                        snackbarHostState.showSnackbar(
                            message = "$city saved to history?",
                            duration = SnackbarDuration.Short
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 Location Text
            Text(
                text = "Weather for: ${selectedLocation ?: "Loading..."}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ✅ Snackbar Host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
