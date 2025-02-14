package com.makaota.weathermzansi.presentation



import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

@Composable
fun LocationSearchBar(onLocationSelected: (String, LatLng) -> Unit) {
    var query by remember { mutableStateOf("Search location...") } // Default placeholder text
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val place = Autocomplete.getPlaceFromIntent(intent)
                query = place.name ?: "Search location..."
                val latLng = place.latLng ?: LatLng(0.0, 0.0)
                onLocationSelected(query, latLng)
            }
        }
    }

    // **🔥 Clickable Text Instead of TextField**
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { // ✅ Clicking the Text triggers Google Places search
                launcher.launch(
                    Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY,
                        listOf(Place.Field.NAME, Place.Field.LAT_LNG)
                    ).setCountry("ZA") // 🇿🇦 South Africa Only
                        .build(context)
                )
            }
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = query,
            color = if (query == "Search location...") Color.Gray else Color.Black,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.weight(1f)) // Pushes the icon to the right

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Location",
            tint = Color.DarkGray
        )
    }
}
