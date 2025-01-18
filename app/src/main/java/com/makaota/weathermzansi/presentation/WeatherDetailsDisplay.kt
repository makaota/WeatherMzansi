package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.makaota.weathermzansi.R
import com.makaota.weathermzansi.ui.theme.DeepBlue

@Composable
fun WeatherDetailsDisplay(
    state: WeatherState,
    modifier: Modifier = Modifier
){
    state.weatherInfo?.currentWeatherData?.let { data ->


        Text(text = "Weather Details",
            color = Color.White,
            fontSize = 20.sp,
            modifier = modifier.padding(start =  16.dp,top =  16.dp))

        Row (verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.fillMaxWidth()
                .padding(16.dp)
                .background(DeepBlue)
                .height(120.dp)
                ){

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "Pressure",
                    color = Color.White,
                    fontSize = 16.sp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_pressure),
                    contentDescription = null,
                    tint = Color.White, // Tinting the icon white
                    modifier = Modifier.size(60.dp)) // Adjust size as needed)
                Text(text = "${data.pressure} hpa",
                    color = Color.LightGray,
                    fontSize = 15.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "Humidity",
                    color = Color.White,
                    fontSize = 16.sp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_drop),
                    contentDescription = null,
                    tint = Color.White, // Tinting the icon white
                    modifier = Modifier.size(60.dp)) // Adjust size as needed))
                Text(text = "${data.humidity} %",
                    color = Color.LightGray,
                    fontSize = 15.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "Wind Speed",
                    color = Color.White,
                    fontSize = 16.sp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_wind),
                    contentDescription = null,
                    tint = Color.White, // Tinting the icon white
                    modifier = Modifier.size(60.dp)) // Adjust size as needed))
                Text(text = "${data.windSpeed} km/h",
                    color = Color.LightGray,
                    fontSize = 15.sp)
            }
        }
    }

}