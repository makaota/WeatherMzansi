package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.domain.location.LocationTracker

@Composable
fun WeatherScreenDisplay(dailyState: DailyWeatherState,
                         combinedWeatherViewModel: CombinedWeatherViewModel,
                         locationTracker: LocationTracker,) {


    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )


    Column{
        when {
            dailyState.dailyWeatherInfo != null -> {
                WeatherScreen(combinedWeatherViewModel = combinedWeatherViewModel,
                    locationTracker = locationTracker)
            }
        }


    }
}

