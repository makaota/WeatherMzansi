package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.makaota.weathermzansi.data.location_database.LocationDao
import kotlinx.coroutines.delay

@Composable
fun WeatherAppWithDrawerDisplay(
    dailyState: DailyWeatherState,
    combinedViewModel: CombinedWeatherViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    locationDao: LocationDao,
    themeViewModel: ThemeViewModel,
    hourlyWeatherState: WeatherState,
) {


    var showError by remember { mutableStateOf(false) }
    var showLoader by remember { mutableStateOf(false) }

    LaunchedEffect(dailyState.error) {
        if (dailyState.error != null) {
            showLoader = true // start loading spinner
            delay(1500)       // simulate a delay
            showLoader = false
            showError = true  // now show the error
        }
    }

        Box(modifier = Modifier.fillMaxSize()) {

            when {
                dailyState.dailyWeatherInfo != null -> {
                    WeatherAppWithDrawer(
                        navController = navController,
                        combinedViewModel = combinedViewModel, locationDao,
                        themeViewModel = themeViewModel,
                        hourlyWeatherState
                    )
                }
                else -> {
                    when {
                        showLoader -> {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

                        showError -> {
                            dailyState.error?.let { error ->
                                Text(
                                    text = error,
                                    color = Color.Red,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
        }
    }
}


