package com.makaota.weathermzansi.presentation

import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.domain.location.LocationTracker
import java.util.Locale

@Composable
fun WeatherScreen(combinedWeatherViewModel: CombinedWeatherViewModel, locationTracker: LocationTracker) {
    var selectedLocation by remember { mutableStateOf<String?>(null) } // 🔥 No Default Value
    var latLng by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current


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
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // 🔥 Search Bar (Overrides Current Location When Used)
        LocationSearchBar { city, coordinates ->
            selectedLocation = city
            latLng = coordinates
            combinedWeatherViewModel.fetchWeather(coordinates.latitude, coordinates.longitude)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Weather for: $selectedLocation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
