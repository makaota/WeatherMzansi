package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun DailyDisplay(viewModel: CombinedWeatherViewModel = hiltViewModel()) {
    val state = viewModel.dailyWeatherState

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        when {
            state.isLoading -> {
                Text("Loading weather data...", style = MaterialTheme.typography.bodySmall, color = Color.White)
            }
            state.error != null -> {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)

            }
            state.dailyWeatherInfo != null -> {
                Text("Daily Weather", fontSize = 20.sp, color = Color.White)
                state.dailyWeatherInfo.dailyWeatherData.forEach { (index, weatherDataList) ->
                    weatherDataList.forEach { data ->
                        DailyWeatherDisplay(data)
                    }
                }
            }
            else -> {
                Text("No weather data to display.")
            }
        }
    }

    Log.d("DailyDisplay", "State: $state")
}

