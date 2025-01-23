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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
                CustomSystemBarColors()
                CurrentWeatherDisplay(state = combinedViewModel.state,
                                dailyState = combinedViewModel.dailyWeatherState)
                //ParallaxEffect()

            }
        }
    }
}


@Composable
fun ParallaxEffect() {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image (will fade but not move)
        Image(
            painter = painterResource(id = R.drawable.day_time),
            contentDescription = "Android logo",
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
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Vivamus euismod purus non nunc varius, id iaculis augue auctor. " +
                        "Ut sollicitudin tincidunt sapien, nec interdum arcu placerat ac." +
                        " Nullam ac magna nec elit cursus maximus. Integer eget dolor non ante" +
                        " dapibus tincidunt sit amet ac urna. Sed volutpat, ipsum nec consequat hendrerit," +
                        " libero risus sodales nunc, vitae vulputate enim ligula ac ligula.\n" +
                        "\n" +
                        "Fusce euismod eros euismod ligula feugiat, at vestibulum lectus bibendum." +
                        " Donec egestas tincidunt orci at varius. In sit amet sapien at lacus vehicula" +
                        " placerat eget eu est. Curabitur sollicitudin, mauris id dictum venenatis," +
                        " erat orci fermentum velit, eget hendrerit elit eros eget elit. Etiam ultricies," +
                        " urna et placerat ultricies, mi sem bibendum ipsum, eu tincidunt turpis eros ac risus.\n" +
                        "\n" +
                        "Morbi id nisl et odio posuere luctus ac id enim. Sed auctor dui neque, " +
                        "vitae laoreet ipsum condimentum vitae. Phasellus vitae fermentum erat. " +
                        "Proin faucibus, felis vel dictum pharetra, velit ipsum dapibus arcu," +
                        " vel dignissim odio nisl et ligula. Etiam euismod, ligula vel sollicitudin feugiat," +
                        " risus lorem hendrerit nulla, nec volutpat mauris est ut velit." +
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. \" +\n" +
                        "                        \"Vivamus euismod purus non nunc varius, id iaculis augue auctor. \" +\n" +
                        "                        \"Ut sollicitudin tincidunt sapien, nec interdum arcu placerat ac.\" +\n" +
                        "                        \" Nullam ac magna nec elit cursus maximus. Integer eget dolor non ante\" +\n" +
                        "                        \" dapibus tincidunt sit amet ac urna. Sed volutpat, ipsum nec consequat hendrerit,\" +\n" +
                        "                        \" libero risus sodales nunc, vitae vulputate enim ligula ac ligula.\\n\" +\n" +
                        "                        \"\\n\" +\n" +
                        "                        \"Fusce euismod eros euismod ligula feugiat, at vestibulum lectus bibendum.\" +\n" +
                        "                        \" Donec egestas tincidunt orci at varius. In sit amet sapien at lacus vehicula\" +\n" +
                        "                        \" placerat eget eu est. Curabitur sollicitudin, mauris id dictum venenatis,\" +\n" +
                        "                        \" erat orci fermentum velit, eget hendrerit elit eros eget elit. Etiam ultricies,\" +\n" +
                        "                        \" urna et placerat ultricies, mi sem bibendum ipsum, eu tincidunt turpis eros ac risus.\\n\" +\n" +
                        "                        \"\\n\" +\n" +
                        "                        \"Morbi id nisl et odio posuere luctus ac id enim. Sed auctor dui neque, \" +\n" +
                        "                        \"vitae laoreet ipsum condimentum vitae. Phasellus vitae fermentum erat. \" +\n" +
                        "                        \"Proin faucibus, felis vel dictum pharetra, velit ipsum dapibus arcu,\" +\n" +
                        "                        \" vel dignissim odio nisl et ligula. Etiam euismod, ligula vel sollicitudin feugiat,\" +\n" +
                        "                        \" risus lorem hendrerit nulla, nec volutpat mauris est ut velit.",
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                color = Color.Black
            )
        }
    }
}

