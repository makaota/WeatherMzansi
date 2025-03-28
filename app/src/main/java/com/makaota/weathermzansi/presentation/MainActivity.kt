package com.makaota.weathermzansi.presentation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.libraries.places.api.Places
import com.makaota.weathermzansi.data.location_database.AppDatabase
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

        // Initialize Places API
        Places.initialize(applicationContext, "AIzaSyBLByYu8IQon0go5ngsHHgU_edGX58NXjU")


        setContent {

            WeatherMzansiTheme {

                val navController = rememberNavController()
                val context = LocalContext.current


                 val db = Room.databaseBuilder(context, AppDatabase::class.java, "weather_db").build()
                  val locationDao = db.locationDao()

                WeatherApp(combinedViewModel = combinedViewModel, navController)
                WeatherAppWithDrawerDisplay(dailyState = combinedViewModel.dailyWeatherState,
                    navController = navController, locationDao = locationDao)

            }
        }
    }
}

@Composable
fun WeatherApp(combinedViewModel: CombinedWeatherViewModel, navHostController: NavHostController) {



    val scrollState = rememberScrollState()

    val isRefreshing by combinedViewModel.isRefreshing.collectAsState() // Observing refresh state



    Box(
        modifier = Modifier
            .fillMaxSize()
          //  .background(backgroundColor2)
    ) {

        // **Fixed: Column Should Have a Limited Size**
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { combinedViewModel.loadWeatherData() }, // Calls ViewModel function
            modifier = Modifier.align(Alignment.Center)
        ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(scrollState)
                .zIndex(1f)
                .background(Color.Transparent)
        ) {
            //    Spacer(modifier = Modifier.height(16.dp))
            WeatherScreenDisplay(
                dailyState = combinedViewModel.dailyWeatherState,
                combinedWeatherViewModel = combinedViewModel
            )
            //    Spacer(modifier = Modifier.height(16.dp))
            CurrentWeatherDisplay(
                hourlyState = combinedViewModel.state,
                dailyState = combinedViewModel.dailyWeatherState
            )
            Spacer(modifier = Modifier.height(16.dp))
            TodayTomorrowWeatherDisplay(dailyState = combinedViewModel.dailyWeatherState)
            //  Spacer(modifier = Modifier.height(16.dp))
            HourlyWeatherForecast(state = combinedViewModel.state)
            DailyDisplay(combinedViewModel, navController = navHostController)
            //  Spacer(modifier = Modifier.height(16.dp))
            WindInfoDisplay(state = combinedViewModel.state)
            Spacer(modifier = Modifier.height(16.dp))
            HumidityInfoDisplay(state = combinedViewModel.state)
            DailyUVIndexDisplay(combinedViewModel.dailyWeatherState)
            DailyDurationDisplay(combinedViewModel.dailyWeatherState)
            Spacer(modifier = Modifier.height(16.dp))

        }

        }

//        // **Show Loading Indicator While Data is Fetching**
//        if (combinedViewModel.state.isLoading) {
//            CircularProgressIndicator(
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }

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




