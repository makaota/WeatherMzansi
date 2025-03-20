package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.data.location_database.LocationDao
import com.makaota.weathermzansi.data.location_database.LocationEntity
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
    locationDao: LocationDao // Room DB Access
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
                                combinedViewModel.updateLocation(city.name, LatLng(city.latitude, city.longitude)) // ✅ Update ViewModel
                                combinedViewModel.fetchWeather(city.latitude, city.longitude) // Fetch weather
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
fun AboutScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("ℹ️ About Screen", fontSize = 24.sp)
    }
}

@Composable
fun CityItem(
    locationEntity: LocationEntity, // Ensure correct `LocationEntity` is passed
    onSelect: () -> Unit,
    onDelete: () -> Unit
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailsScreen(viewModel: CombinedWeatherViewModel, navController: NavHostController) {
    val selectedWeatherData = viewModel.selectedWeatherData // Observe selected data

    val state = viewModel.dailyWeatherState
    val textColor = if (isSystemInDarkTheme()) colorResource(id = R.color.white)
    else colorResource(id = R.color.dark_gray)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            state.dailyWeatherInfo?.dailyWeatherData?.forEach { (index, weatherDataList) ->
                items(weatherDataList) { data ->
                    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
                    val today = LocalDate.now()
                    val formattedDay = if (data.time == today) "Today" else data.time.format(dayFormatter)

                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .clickable {
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
                            color = textColor
                        )

                        // **Middle: Weather Icon**
                        Image(
                            painter = painterResource(id = data.weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                        // **Bottom: High / Low Temperature**
                        Text(
                            text = "${data.maxTemperatures.roundToInt()}°/${data.lowTemperatures.roundToInt()}°",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor
                        )
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray.copy(alpha = 0.5f))) {

            Text(text = "Daytime",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            )

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){

                Image(
                    painter = painterResource(id = selectedWeatherData?.weatherType!!.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${selectedWeatherData.maxTemperatures.roundToInt()}°C",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

            }
            Text(
                text = selectedWeatherData?.weatherType!!.weatherDesc,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

        }



        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.temperature_feels_like_ic),
            text = "Feels Like",
            textColor = textColor,
            textValue = "${selectedWeatherData?.apparentTemperatureMax!!.roundToInt()}°"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.sunrise_up_ic),
            text = "Sunrise",
            textColor = textColor,
            textValue = "${selectedWeatherData?.sunrise}"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.sunset_down_ic),
            text = "Sunset",
            textColor = textColor,
            textValue = "${selectedWeatherData?.sunset}"
        )
        Spacer(modifier = Modifier.height(16.dp))
        val dayLightHours = selectedWeatherData?.daylightDuration?.div(3600)!!.toInt()
        val dayLightMinutes = (selectedWeatherData?.daylightDuration % (3600) / 60).toInt()
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.time_ic),
            text = "Daylight Duration",
            textColor = textColor,
            textValue = "${dayLightHours}h ${dayLightMinutes}m"
        )
        Spacer(modifier = Modifier.height(16.dp))
        val sunShineHours = selectedWeatherData?.sunshineDuration?.div(3600)!!.toInt()
        val sunShineMinutes = (selectedWeatherData?.sunshineDuration % (3600) / 60).toInt()
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.clear_day_ic),
            text = "Sunshine Duration",
            textColor = textColor,
            textValue = "${sunShineHours}h ${sunShineMinutes}m"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.uv_index_alt_ic),
            text = "UV Index Max",
            textColor = textColor,
            textValue = selectedWeatherData?.uvIndex!!.roundToInt().toString()
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.uv_index_ic),
            text = "UV Index Clear Sky",
            textColor = textColor,
            textValue = selectedWeatherData?.uvIndexClearSky!!.roundToInt().toString()
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.rain_ic),
            text = "Rain Chances",
            textColor = textColor,
            textValue = "${selectedWeatherData?.chancesOfRain!!.roundToInt()}%"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.snow_ic),
            text = "Snowfall Sum",
            textColor = textColor,
            textValue = "${selectedWeatherData?.snowfallSum}mm"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.wind_speed_ic),
            text = "Max Wind Speed (10m)",
            textColor = textColor,
            textValue = "${selectedWeatherData?.windSpeed!!.roundToInt()}km/h"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.wind_gust),
            text = "Max Wind Gust (10m)",
            textColor = textColor,
            textValue = "${selectedWeatherData?.windGust!!.roundToInt()}km/h"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.compass_ic),
            text = "Dom Wind Direction (10m)",
            textColor = textColor,
            textValue = "${getWindDirection(selectedWeatherData.windDirection)} " +
                    "(${selectedWeatherData.windDirection.roundToInt()}°)"
        )



        // NIGHT TIME SECTION


        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray.copy(alpha = 0.5f))) {

            Text(text = "Night Time",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            )

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically){

                Image(
                    painter = painterResource(id = selectedWeatherData?.weatherType!!.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${selectedWeatherData.lowTemperatures.roundToInt()}°C",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

            }
            Text(
                text = selectedWeatherData?.weatherType!!.weatherDesc,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

        }



        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.temperature_feels_like_ic),
            text = "Feels Like",
            textColor = textColor,
            textValue = "${selectedWeatherData?.apparentTemperatureMin!!.roundToInt()}°"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.wind_speed),
            text = "Wind Speed",
            textColor = textColor,
            textValue = "${selectedWeatherData?.windSpeed!!.roundToInt()}km/h"
        )
        Spacer(modifier = Modifier.height(16.dp))
        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_wind),
            text = "Wind Gust",
            textColor = textColor,
            textValue = "${selectedWeatherData?.windGust!!.roundToInt()}km/h"
        )
        Spacer(modifier = Modifier.height(16.dp))
        val dayHours = selectedWeatherData?.daylightDuration?.div(3600)!!.toInt()
        val dayMinutes = (selectedWeatherData?.daylightDuration % (3600) / 60).toInt()
        val totalNightMinutes = (24 * 60) - (dayHours * 60 + dayMinutes)
        val nightHours = totalNightMinutes / 60
        val nightMinutes = totalNightMinutes % 60

        DayAndNightDisplay(
            imageVector = ImageVector.vectorResource(id = R.drawable.time_ic),
            text = "Night Duration",
            textColor = textColor,
            textValue = "${nightHours}h ${nightMinutes}m"
        )








    }
}

@Composable
fun DayAndNightDisplay(imageVector: ImageVector, text: String, textColor: Color, textValue: String){

    Row( horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray.copy(alpha = 0.5f))) {

        Image(imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .padding(start = 16.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
           // fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.padding(start = 16.dp)
        )
        Column(horizontalAlignment = Alignment.End,
            modifier = Modifier.weight(1f)) {
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

