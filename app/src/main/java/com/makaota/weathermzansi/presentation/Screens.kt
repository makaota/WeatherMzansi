package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.data.location_database.LocationDao
import com.makaota.weathermzansi.data.location_database.LocationEntity
import com.makaota.weathermzansi.domain.utils.ThemeColors
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("🏠 Home Screen", fontSize = 24.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityManagementScreen(
    combinedViewModel: CombinedWeatherViewModel,
    navController: NavHostController,
    locationDao: LocationDao, // Room DB Access
) {
    val scope = rememberCoroutineScope()

    // Correctly collect locations from Room Database
    val cities by locationDao.getAllLocations().collectAsState(initial = emptyList())


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (cities.isEmpty()) {
            Text(
                text = "No saved locations",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                fontSize = 18.sp,
                color = Color.Gray
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                // Correctly iterate over cities list
                items(cities) { city ->
                    CityItem(
                        locationEntity = city, // Pass each LocationEntity
                        onSelect = {
                            combinedViewModel.updateLocation(
                                city.name,
                                LatLng(city.latitude, city.longitude)
                            ) // Update ViewModel
                            combinedViewModel.fetchWeather(
                                city.latitude,
                                city.longitude
                            ) // Fetch weather
                            navController.navigateUp() // Navigate back to home
                        },
                        onDelete = {
                            scope.launch {
                                locationDao.deleteLocation(city) // Delete city from DB
                            }
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun SettingsScreen(themeViewModel: ThemeViewModel = viewModel()) {
    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(backgroundColor)
    ) {
        Text("Theme Settings", color = textColor)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dark Mode")
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { themeViewModel.toggleTheme(it) }
            )
        }
    }
}

@Composable
fun CityItem(
    locationEntity: LocationEntity, // Ensure correct `LocationEntity` is passed
    onSelect: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onSelect() }, // Select city when clicked
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = locationEntity.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Lat: ${locationEntity.latitude}, Lng: ${locationEntity.longitude}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red // Delete icon in red
            )
        }
    }
}

@Composable
fun WeatherDetailsScreen(
    viewModel: CombinedWeatherViewModel, navController: NavHostController,
    themeViewModel: ThemeViewModel, hourlyState: WeatherState,
) {
    val selectedWeatherData = viewModel.selectedWeatherData // Observe selected data

    val dailyWeatherState = viewModel.dailyWeatherState

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val backgroundColor2 = ThemeColors.backgroundColor2(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    val scrollState = rememberScrollState()

    // val selectedIndex = viewModel.getSelectedDay()

    val lazyListState = rememberLazyListState() // State for LazyRow

    // **Track Previous & Current Day for Transition Animation**
    var previousDayIndex by remember { mutableStateOf(viewModel.getSelectedDay()) }
    val selectedIndex by rememberUpdatedState(viewModel.getSelectedDay())

    // var previousDayIndex by remember { mutableStateOf(selectedIndex) } // Track previous day
    val transitionDirection = remember(selectedWeatherData) {
        if (selectedIndex > previousDayIndex) AnimatedContentTransitionScope.SlideDirection.Left
        else AnimatedContentTransitionScope.SlideDirection.Right
    }

    // **Auto-scroll when a new day is selected**
    LaunchedEffect(selectedIndex) {
        previousDayIndex = selectedIndex
        lazyListState.animateScrollToItem(selectedIndex)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor2)
            .padding(16.dp)
        //.verticalScroll(scrollState)
    ) {

        LazyRow(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            dailyWeatherState.dailyWeatherInfo?.dailyWeatherData?.forEach { (index, weatherDataList) ->
                items(weatherDataList) { data ->
                    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
                    val today = LocalDate.now()
                    val formattedDay =
                        if (data.time == today) "Today" else data.time.format(dayFormatter)

                    val isSelected = selectedIndex == index

                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isSelected) colorResource(id = R.color.orange) else backgroundColor
                            )
                            .clickable {
                                viewModel.selectDay(index)
                                Log.d("WeatherDetailsScreen", "Selected index: $selectedIndex")
                                viewModel.selectWeatherData(data) //Change selected data
                            }
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // **Top: Day Name (Today, Wednesday, etc.)**
                        Text(
                            text = formattedDay,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else textColor
                        )

                        // **Middle: Weather Icon**
                        Image(
                            painter = painterResource(id = data.weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                        // **Bottom: High / Low Temperature**
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = colorResource(id = R.color.orange_red))) {
                                    append("${data.maxTemperatures.roundToInt()}°")
                                }
                                append("/") // Separator
                                withStyle(style = SpanStyle(color = colorResource(id = R.color.dodger_blue))) {
                                    append("${data.lowTemperatures.roundToInt()}°")
                                }
                            },
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            when {
                hourlyState.weatherInfo != null -> {
                    Text(
                        text = "Hourly Forecast",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            AnimatedContent(targetState = hourlyState, transitionSpec = {
                slideIntoContainer(transitionDirection, tween(500)) togetherWith
                        slideOutOfContainer(transitionDirection, tween(500))
            }, label = "") { data ->
                data?.let { data ->


                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        hourlyState.weatherInfo?.weatherDataPerDay?.get(selectedIndex)
                            ?.let { data ->

                                LazyRow(
                                    userScrollEnabled = true,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp)) // Apply rounded corners to LazyRow
                                        .background(backgroundColor)    // Ensure the background matches
                                        .padding(8.dp),
                                ) {
                                    items(data) { weatherData ->

                                        HourlyWeatherDisplay(
                                            weatherData = weatherData,
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .background(Color.Transparent),
                                            themeViewModel = themeViewModel
                                        )
                                    }
                                }
                            }
                    }

                }
            }

            hourlyState.weatherInfo?.weatherDataPerDay?.get(selectedIndex).let { data ->

                Text(
                    text = "Hourly Weather Details",
                    fontSize = 20.sp,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    modifier= Modifier.padding(top = 16.dp, bottom = 16.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 180.dp, max = 230.dp) // Adjust height dynamically

                    ) {
                        WindDirectionCompass(
                            windDegrees = data?.get(selectedIndex)?.windDirection!!.toFloat(),
                            windSpeed = data.get(selectedIndex).windSpeed.dec(),
                            themeViewModel = themeViewModel
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp)) // Space between two cards

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 180.dp, max = 230.dp) // Adjust height dynamically

                    ) {
                        PressureGauge(
                            pressure = data?.get(selectedIndex)?.pressure!!.toFloat(),
                            themeViewModel = themeViewModel
                        )
                    }
                }

            }

            hourlyState.weatherInfo?.weatherDataPerDay?.get(selectedIndex).let { data ->

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 180.dp, max = 230.dp) // Adjust height dynamically

                    ) {
                        HumidityGauge(
                            humidity = data?.get(selectedIndex)?.humidity!!.toFloat(),
                            themeViewModel = themeViewModel
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp)) // Space between two cards

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(230.dp)

                    ) {
                        VisibilityBlurEffect(
                            visibilityKm = data?.get(selectedIndex)?.visibility!!.toFloat(),
                            themeViewModel = themeViewModel
                        )
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
                // .verticalScroll(scrollState)
            ) {

                AnimatedContent(targetState = selectedWeatherData, transitionSpec = {
                    slideIntoContainer(transitionDirection, tween(500)) togetherWith
                            slideOutOfContainer(transitionDirection, tween(500))
                }, label = "") { data ->
                    data?.let { data ->


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(backgroundColor)
                            //.alpha(animatedAlpha)
                        ) {

                            Text(
                                text = "Daytime",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.orange_red),
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(16.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = data.weatherType.iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${data.maxTemperatures.roundToInt()}°C",
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.orange_red)
                                )

                            }
                            Text(
                                text = data.weatherType.weatherDesc,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                        }
                    }
                }



                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(targetState = selectedWeatherData, transitionSpec = {
                    slideIntoContainer(transitionDirection, tween(500)) togetherWith
                            slideOutOfContainer(transitionDirection, tween(500))
                }, label = "") { data ->
                    data?.let { data ->

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                            //  .background(Color.LightGray.copy(alpha = 0.5f))
                            //.alpha(animatedAlpha)
                        ) {


                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.temperature_feels_like_ic),
                                text = "Feels Like",
                                textColor = textColor,
                                textValue = "${data.apparentTemperatureMax.roundToInt()}°",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.sunrise_up_ic),
                                text = "Sunrise",
                                textColor = textColor,
                                textValue = "${data.sunrise}",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.sunset_down_ic),
                                text = "Sunset",
                                textColor = textColor,
                                textValue = "${data.sunset}",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            val dayLightHours =
                                data.daylightDuration.div(3600).toInt()
                            val dayLightMinutes =
                                (data.daylightDuration % (3600) / 60).toInt()
                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.time_ic),
                                text = "Daylight Duration",
                                textColor = textColor,
                                textValue = "${dayLightHours}h ${dayLightMinutes}m",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            val sunShineHours =
                                data.sunshineDuration.div(3600).toInt()
                            val sunShineMinutes =
                                (data.sunshineDuration % (3600) / 60).toInt()
                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.clear_day_ic),
                                text = "Sunshine Duration",
                                textColor = textColor,
                                textValue = "${sunShineHours}h ${sunShineMinutes}m",
                                themeViewModel = themeViewModel
                            )


                        }
                    }
                }

                // NIGHT TIME SECTION


                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(targetState = selectedWeatherData, transitionSpec = {
                    slideIntoContainer(transitionDirection, tween(500)) togetherWith
                            slideOutOfContainer(transitionDirection, tween(500))
                }, label = "") { data ->
                    data?.let { data ->

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(backgroundColor)
                        ) {

                            Text(
                                text = "Night Time",
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.dodger_blue),
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(16.dp)
                            )

                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = data.weatherType.iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${data.lowTemperatures.roundToInt()}°C",
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.dodger_blue)
                                )

                            }
                            Text(
                                text = data.weatherType.weatherDesc,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedContent(targetState = selectedWeatherData, transitionSpec = {
                    slideIntoContainer(transitionDirection, tween(500)) togetherWith
                            slideOutOfContainer(transitionDirection, tween(500))
                }, label = "") { data ->
                    data?.let { data ->

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                        ) {

                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.temperature_feels_like_ic),
                                text = "Feels Like",
                                textColor = textColor,
                                textValue = "${data.apparentTemperatureMin.roundToInt()}°",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.wind_speed),
                                text = "Wind Speed",
                                textColor = textColor,
                                textValue = "${data.windSpeed.roundToInt()}km/h",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_wind),
                                text = "Wind Gust",
                                textColor = textColor,
                                textValue = "${data.windGust.roundToInt()}km/h",
                                themeViewModel = themeViewModel
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            val dayHours = data.daylightDuration.div(3600).toInt()
                            val dayMinutes =
                                (data.daylightDuration % (3600) / 60).toInt()
                            val totalNightMinutes = (24 * 60) - (dayHours * 60 + dayMinutes)
                            val nightHours = totalNightMinutes / 60
                            val nightMinutes = totalNightMinutes % 60

                            DayAndNightDisplay(
                                imageVector = ImageVector.vectorResource(id = R.drawable.time_ic),
                                text = "Night Duration",
                                textColor = textColor,
                                textValue = "${nightHours}h ${nightMinutes}m",
                                themeViewModel = themeViewModel
                            )

                        }
                    }
                }
            }

        }
    }
}

@Composable
fun DayAndNightDisplay(
    imageVector: ImageVector,
    text: String,
    textColor: Color,
    textValue: String,
    themeViewModel: ThemeViewModel,
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)

    // val textColor = ThemeColors.textColor(isDarkTheme)
    val backgroundColor = ThemeColors.backgroundColor(isDarkTheme)
    val labelColor = ThemeColors.labelColor(isDarkTheme)

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
    ) {

        Image(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .padding(start = 16.dp),
            colorFilter = ColorFilter.tint(textColor)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            // fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(start = 16.dp)
        )
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = textValue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp)
            )
        }
    }

}

