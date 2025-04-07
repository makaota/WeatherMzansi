package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.makaota.weathermzansi.domain.utils.ThemeColors


@Composable
fun DailyDisplay(viewModel: CombinedWeatherViewModel = hiltViewModel(), navController: NavController,
                 themeViewModel: ThemeViewModel) {

    val state = viewModel.dailyWeatherState


    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {

        when {
            state.dailyWeatherInfo != null -> {
                Text(
                    text ="7 days weather forecast",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn (modifier = Modifier.height(382.dp)){
                    state.dailyWeatherInfo.dailyWeatherData.forEach { (index, weatherDataList) ->
                        items(weatherDataList) { data ->
                            DailyWeatherDisplay(
                                index, data,
                                modifier = Modifier
                                    .clickable {
                                        Log.d("DailyDisplay", "index: $index")
                                        viewModel.selectDay(index)
                                        viewModel.selectWeatherData(data)
                                         navController.navigate("WeatherDetails")
                                    }, themeViewModel = themeViewModel
                            )
                            // Add space after each row
                            Spacer(modifier = Modifier.height(8.dp))
                            Log.d("DailyDisplay", "data: $data")

                        }
                    }
                }
            }
        }
    }

    Log.d("DailyDisplay", "State: $state")
}

