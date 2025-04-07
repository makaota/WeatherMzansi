package com.makaota.weathermzansi.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.libraries.places.api.Places
import com.makaota.weathermzansi.data.location_database.AppDatabase
import com.makaota.weathermzansi.data.theme_datastore.DataStoreManager
import com.makaota.weathermzansi.domain.utils.ThemeColors
import com.makaota.weathermzansi.ui.theme.WeatherMzansiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private val combinedViewModel: CombinedWeatherViewModel by viewModels()
  //  private val themeViewModel: ThemeViewModel by viewModels()

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

        // Initialize Places API
        Places.initialize(applicationContext, "AIzaSyBLByYu8IQon0go5ngsHHgU_edGX58NXjU")




        setContent {

            val systemDarkTheme = isSystemInDarkTheme()

            val dataStoreManager = DataStoreManager(applicationContext)

            val themeViewModel: ThemeViewModel by viewModels {
                ThemeViewModelFactory(dataStoreManager, systemDarkTheme)
            }
            val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)
            WeatherMzansiTheme(darkTheme = isDarkTheme) {

                val navController = rememberNavController()
                val context = LocalContext.current


                 val db = Room.databaseBuilder(context, AppDatabase::class.java, "weather_db").build()
                  val locationDao = db.locationDao()




                WeatherApp(combinedViewModel = combinedViewModel, navController,themeViewModel)
                WeatherAppWithDrawerDisplay(dailyState = combinedViewModel.dailyWeatherState,
                    navController = navController, locationDao = locationDao,
                    themeViewModel = themeViewModel, hourlyWeatherState = combinedViewModel.state)

            }
        }
    }
}

@Composable
fun WeatherApp(combinedViewModel: CombinedWeatherViewModel, navHostController: NavHostController,
               themeViewModel: ThemeViewModel) {


    val isRefreshing by combinedViewModel.isRefreshing.collectAsState() // Observing refresh state

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor2 = ThemeColors.backgroundColor2(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)





    Box (modifier = Modifier.fillMaxSize()){
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { combinedViewModel.loadWeatherData() },
         //   modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor2)
            ) {
                item {
                    WeatherScreenDisplay(
                        dailyState = combinedViewModel.dailyWeatherState,
                        combinedWeatherViewModel = combinedViewModel,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    CurrentWeatherDisplay(
                        hourlyState = combinedViewModel.state,
                        dailyState = combinedViewModel.dailyWeatherState,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    TodayTomorrowWeatherDisplay(
                        dailyState = combinedViewModel.dailyWeatherState,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    HourlyWeatherForecast(
                        state = combinedViewModel.state,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    DailyDisplay(
                        combinedViewModel,
                        navController = navHostController,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    WindInfoDisplay(
                        state = combinedViewModel.state,
                        themeViewModel = themeViewModel
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    HumidityInfoDisplay(
                        state = combinedViewModel.state,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    DailyUVIndexDisplay(
                        combinedViewModel.dailyWeatherState,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    DailyDurationDisplay(
                        combinedViewModel.dailyWeatherState,
                        themeViewModel = themeViewModel
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }


            }
        }

        // **Show Loading Indicator While Data is Fetching**
        if (combinedViewModel.state.isLoading && !isRefreshing) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // **Show Error Message if an Error Occurred**
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




