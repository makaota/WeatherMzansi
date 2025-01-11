package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makaota.weathermzansi.domain.location.LocationTracker
import com.makaota.weathermzansi.domain.repository.WeatherRepository
import com.makaota.weathermzansi.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class DailyWeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var dailyWeatherState by mutableStateOf(DailyWeatherState())
        private set

    fun loadDailyWeatherInfo() {
        viewModelScope.launch {
            dailyWeatherState = dailyWeatherState.copy(isLoading = true, error = null)

            val location = locationTracker.getCurrentLocation()
            if (location == null) {
                dailyWeatherState = dailyWeatherState.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Please enable GPS and grant location permissions."
                )
                return@launch
            }

            when (val result = repository.getDailyWeatherData(location.latitude, location.longitude)) {
                is Resource.Success -> {
                    result.data?.let { dailyWeatherInfo ->
                        dailyWeatherState = dailyWeatherState.copy(
                            dailyWeatherInfo = dailyWeatherInfo,
                            isLoading = false,
                            error = null
                        )
                    } ?: run {
                        dailyWeatherState = dailyWeatherState.copy(
                            isLoading = false,
                            error = "No weather data available."
                        )
                    }
                }
                is Resource.Error -> {
                    dailyWeatherState = dailyWeatherState.copy(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred."
                    )
                }
            }
        }
    }
}





//@HiltViewModel
//class DailyWeatherViewModel @Inject constructor(
//    private val repository: WeatherRepository,
//    private val locationTracker: LocationTracker
//): ViewModel() {
//
//    var dailyWeatherState by mutableStateOf(DailyWeatherState())
//        private set
//
//    fun loadDailyWeatherInfo() {
//        viewModelScope.launch {
//            dailyWeatherState = dailyWeatherState.copy(
//                isLoading = true,
//                error = null
//            )
//
//            locationTracker.getCurrentLocation()?.let { location ->
//                when (val result = repository.getDailyWeatherData(location.latitude, location.longitude)) {
//                    is Resource.Success -> {
//                        result.data?.let { dailyWeatherInfo ->
//                            // Transform the data to map structure
//                            val mappedData = dailyWeatherInfo.dailyWeatherData.entries.mapIndexed { index, entry ->
//                                index to entry.value
//                            }.toMap()
//
//                            Log.d("DailyWeatherViewModel", "Mapped Data: $mappedData")
//
//                            // Update state with transformed data
//                            dailyWeatherState = dailyWeatherState.copy(
//                                dailyWeatherInfo = dailyWeatherInfo.copy(dailyWeatherData = mappedData),
//                                isLoading = false,
//                                error = null
//                            )
//                        }
//                    }
//
//                    is Resource.Error -> {
//                        dailyWeatherState = dailyWeatherState.copy(
//                            dailyWeatherInfo = null,
//                            isLoading = false,
//                            error = result.message
//                        )
//                    }
//
//                    else -> {}
//                }
//            } ?: kotlin.run {
//                dailyWeatherState = dailyWeatherState.copy(
//                    isLoading = false,
//                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
//                )
//            }
//        }
//    }
//
//}