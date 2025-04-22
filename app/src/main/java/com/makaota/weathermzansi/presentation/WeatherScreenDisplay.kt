package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.domain.utils.ThemeColors

@Composable
fun WeatherScreenDisplay(
    dailyState: DailyWeatherState,
    combinedWeatherViewModel: CombinedWeatherViewModel,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val selectedLocation by combinedWeatherViewModel.selectedLocation.collectAsState()

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        when {
            dailyState.dailyWeatherInfo != null -> {
                Text(text = " ${selectedLocation ?: "Loading..."}",
                    color = textColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 25.sp)
            }
        }
    }
}

