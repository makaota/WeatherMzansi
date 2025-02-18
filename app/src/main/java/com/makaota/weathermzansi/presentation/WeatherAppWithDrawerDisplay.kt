package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.data.location_database.LocationDao

@Composable
fun WeatherAppWithDrawerDisplay(dailyState: DailyWeatherState,
                                viewModel: CombinedWeatherViewModel = hiltViewModel(),
                                navController: NavHostController = rememberNavController(),
                                locationDao: LocationDao
) {




    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )


    Column{


        when {
            dailyState.dailyWeatherInfo != null -> {
                WeatherAppWithDrawer(navController = navController,
                    combinedViewModel = viewModel,locationDao)
            }
        }


    }
}
