package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.data.location_database.LocationDao
import com.makaota.weathermzansi.data.location_database.LocationEntity
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("🏠 Home Screen", fontSize = 24.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityManagementScreen(
    combinedViewModel: CombinedWeatherViewModel,
    navController: NavHostController,
    locationDao: LocationDao // ✅ Room DB Access
) {
    val scope = rememberCoroutineScope()

    // ✅ Correctly collect locations from Room Database
    val cities by locationDao.getAllLocations().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Cities") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (cities.isEmpty()) {
                Text(
                    text = "No saved locations",
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // ✅ Correctly iterate over cities list
                    items(cities) { city ->
                        CityItem(
                            locationEntity = city, // ✅ Pass each LocationEntity
                            onSelect = {
                                combinedViewModel.updateLocation(city.name, LatLng(city.latitude, city.longitude)) // ✅ Update ViewModel
                                combinedViewModel.fetchWeather(city.latitude, city.longitude) // ✅ Fetch weather
                                navController.navigateUp() // ✅ Navigate back to home
                            },
                            onDelete = {
                                scope.launch {
                                    locationDao.deleteLocation(city) // ✅ Delete city from DB
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AboutScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("ℹ️ About Screen", fontSize = 24.sp)
    }
}

@Composable
fun CityItem(
    locationEntity: LocationEntity, // ✅ Ensure correct `LocationEntity` is passed
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onSelect() }, // ✅ Select city when clicked
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = locationEntity.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Lat: ${locationEntity.latitude}, Lng: ${locationEntity.longitude}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red // ✅ Delete icon in red
            )
        }
    }
}
