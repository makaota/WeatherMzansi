package com.makaota.weathermzansi.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.makaota.weathermzansi.domain.utils.ThemeColors


@Composable
fun DrawerContent(navController: NavController, closeDrawer: () -> Unit, themeViewModel: ThemeViewModel,) {
    val selectedItem = remember { mutableStateOf("home") }

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)
    val backgroundColor2 = ThemeColors.backgroundColor2(isDarkTheme)
    val textColor = ThemeColors.textColor(isDarkTheme)

    Column(modifier = Modifier.fillMaxSize().background(backgroundColor2)) {
        Text(
            text = "Menu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(16.dp)
        )

        DrawerItem("Location Management", "cityManagement", Icons.Default.Place, selectedItem, navController, closeDrawer,themeViewModel)
        DrawerItem("Settings", "settingsScreen", Icons.Default.Settings, selectedItem, navController, closeDrawer,themeViewModel)
    }
}

@Composable
fun DrawerItem(
    text: String,
    route: String,
    icon: ImageVector,
    selectedItem: MutableState<String>,
    navController: NavController,
    closeDrawer: () -> Unit,
    themeViewModel: ThemeViewModel
) {

    val isDarkTheme by themeViewModel.isDarkTheme.observeAsState(false)
    val backgroundColor1 = ThemeColors.backgroundColor(isDarkTheme)
    val textColor1 = ThemeColors.textColor(isDarkTheme)

    val isSelected = selectedItem.value == route
    val backgroundColor = if (isSelected) Color(0xFF87CEEB) else Color.Transparent
    val textColor = if (isSelected) Color.White else textColor1


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (navController.currentDestination?.route != route) {
                    navController.navigate(route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                    selectedItem.value = route
                    closeDrawer()
                }
            }
            .background(backgroundColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = textColor, textAlign = TextAlign.End)
        Spacer(modifier = Modifier.width(12.dp))
        Icon(imageVector = icon, contentDescription = text, tint = textColor, modifier = Modifier.size(24.dp))
    }
}
