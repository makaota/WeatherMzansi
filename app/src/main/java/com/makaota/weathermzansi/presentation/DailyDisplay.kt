package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.makaota.weathermzansi.R


@Composable
fun DailyDisplay(viewModel: CombinedWeatherViewModel = hiltViewModel()) {
    val state = viewModel.dailyWeatherState


    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        when {
            state.dailyWeatherInfo != null -> {
                Text("7 days weather forecast",
                    fontSize = 16.sp,
                    color = textColor)
                Spacer(modifier = Modifier.height(16.dp))
                state.dailyWeatherInfo.dailyWeatherData.forEach { (index, weatherDataList) ->
                    weatherDataList.forEach { data ->
                        DailyWeatherDisplay(data, modifier = Modifier.background(Color.Transparent))
                        // Add space after each row
                        Spacer(modifier = Modifier.height(8.dp))
                        Log.d("DailyDisplay", "data: $data")
                    }
                }
            }
        }
    }

    Log.d("DailyDisplay", "State: $state")
}

