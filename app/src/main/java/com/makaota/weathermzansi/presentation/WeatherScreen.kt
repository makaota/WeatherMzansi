package com.makaota.weathermzansi.presentation

import android.location.Geocoder
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.location.LocationTracker
import java.util.Locale

@Composable
fun WeatherScreen(
    combinedWeatherViewModel: CombinedWeatherViewModel,
    locationTracker: LocationTracker,
) {


    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.white
    )

    val labelColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
        else colorResource(id = R.color.medium_gray)

    val backgroundColor =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
        else colorResource(
            id = R.color.sky_blue
        )

    val backgroundColor2 =
        if (isSystemInDarkTheme()) colorResource(id = R.color.night_image_dark)
        else colorResource(
            id = R.color.day_image_dark_blue
        )

    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var latLng by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current

    // Get User’s Current Location When Screen Loads
    LaunchedEffect(Unit) {
        val location = locationTracker.getCurrentLocation()
        if (location != null) {
            latLng = LatLng(location.latitude, location.longitude)
            combinedWeatherViewModel.fetchWeather(location.latitude, location.longitude)

            // Reverse Geocode to Get City Name
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

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            // Location Text
            Text(
                text = " ${selectedLocation ?: "Loading..."}",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )
        }

    }
}
