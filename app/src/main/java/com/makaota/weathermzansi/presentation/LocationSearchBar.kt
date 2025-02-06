package com.makaota.weathermzansi.presentation



import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

@Composable
fun LocationSearchBar(onLocationSelected: (String, LatLng) -> Unit) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val place = Autocomplete.getPlaceFromIntent(intent)

                query = place.name ?: ""
                val latLng = place.latLng ?: LatLng(0.0, 0.0)

                onLocationSelected(query, latLng)
            }
        }
    }


    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = { Text("Search location...") },
        trailingIcon = {
            IconButton(onClick = {
                launcher.launch(
                    Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY,
                        listOf(Place.Field.NAME, Place.Field.LAT_LNG))
                        .setCountry("ZA") // 🇿🇦 South Africa Only
                        .build(context)
                )
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search Location")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

fun fetchWeather(latitude: Double, longitude: Double) {
    val url = "https://api.weather.com/daily?lat=$latitude&lon=$longitude&timezone=Africa/Johannesburg"


}

