package com.makaota.weathermzansi.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.ui.theme.WeatherMzansiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val combinedViewModel: CombinedWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            combinedViewModel.loadWeatherData()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        setContent {

            WeatherMzansiTheme {


//                val isRefreshing = combinedViewModel.isRefreshing.collectAsState().value
//
//                SwipeRefresh(
//                    state = rememberSwipeRefreshState(isRefreshing),
//                    onRefresh = { combinedViewModel.loadWeatherData() }
//                ) {
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(DarkBlue)
//                    ) {
//
//                        Column(
//                            modifier = Modifier
//                                .verticalScroll(rememberScrollState())
//                                .wrapContentHeight()
//
//                        ) {
//                            WeatherCard(
//                                state = combinedViewModel.state,
//                                backgroundColor = DeepBlue,
//                                dailyState = combinedViewModel.dailyWeatherState
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                            WeatherForecast(state = combinedViewModel.state)
//                            Spacer(modifier = Modifier.height(16.dp))
//                            DailyDisplay(combinedViewModel)
//                            WeatherDetailsDisplay(state = combinedViewModel.state)
//                        }
//                        if (combinedViewModel.state.isLoading) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//                        combinedViewModel.state.error?.let { error ->
//                            Text(
//                                text = error,
//                                color = Color.Red,
//                                textAlign = TextAlign.Center,
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//
//                    }
//                }
//                CustomSystemBarColors()
//                CurrentWeatherDisplay(hourlyState = combinedViewModel.state,
//                    dailyState = combinedViewModel.dailyWeatherState)
//                TodayTomorrowWeatherDisplay(dailyState = combinedViewModel.dailyWeatherState)
                ParallaxEffect()
                //HourlyWeatherForecast(state = combinedViewModel.state)
               // DailyDisplay(combinedViewModel)

            }
        }
    }
}


@Composable
fun ParallaxEffect(combinedViewModel: CombinedWeatherViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()


    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(
        id = R.color.dark_gray
    )

    val labelColor = if (isSystemInDarkTheme()) colorResource(id = R.color.light_steel_blue)
    else colorResource(id = R.color.medium_gray)

    val backgroundColor = if (isSystemInDarkTheme()) colorResource(id = R.color.night_sky_blue)
    else colorResource(
        id = R.color.sky_blue
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image (will fade but not move)
        Image(
            painter = painterResource(id = R.drawable.day_time),
            contentDescription = "Camping Image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    // Adjust alpha based on scroll position
                    alpha = 1f - (scrollState.value / 1000f).coerceIn(0f, 1f)
                }
        )

        // Foreground Text (scrollable)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .zIndex(1f) // Ensures the text is above the image
                .background(Color.Transparent) // Make the text layout transparent
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            CurrentWeatherDisplay(hourlyState = combinedViewModel.state,
                dailyState = combinedViewModel.dailyWeatherState)
            Spacer(modifier = Modifier.height(50.dp))
            TodayTomorrowWeatherDisplay(dailyState = combinedViewModel.dailyWeatherState)
           // Spacer(modifier = Modifier.height(16.dp))
            HourlyWeatherForecast(state = combinedViewModel.state)
            DailyDisplay(combinedViewModel)
        }
        if (combinedViewModel.state.isLoading) {
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

