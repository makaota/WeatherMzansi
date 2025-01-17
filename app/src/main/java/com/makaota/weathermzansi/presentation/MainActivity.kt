package com.makaota.weathermzansi.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.ui.theme.DarkBlue
import com.makaota.weathermzansi.ui.theme.DeepBlue
import com.makaota.weathermzansi.ui.theme.WeatherMzansiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val viewModel: WeatherViewModel by viewModels()
   // private val dailyViewModel: DailyWeatherViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val combinedViewModel: CombinedWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            combinedViewModel.loadWeatherData()
        }
        permissionLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ))

        setContent {
            WeatherMzansiTheme {

                Box(modifier = Modifier.fillMaxSize()
                    .background(DarkBlue)){

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .wrapContentHeight()

                    ) {
                        WeatherCard(
                            state = combinedViewModel.state,
                            backgroundColor = DeepBlue,
                            dailyState = combinedViewModel.dailyWeatherState
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        WeatherForecast(state = combinedViewModel.state)
                        Spacer(modifier = Modifier.height(16.dp))
                        DailyDisplay(combinedViewModel)
                    }
                    if(combinedViewModel.state.isLoading) {
                        CircularProgressIndicator(
                             modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    combinedViewModel.state.error?.let { error ->
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
