package com.makaota.weathermzansi.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.makaota.weathermzansi.domain.location.LocationTracker
import com.makaota.weathermzansi.domain.repository.WeatherRepository
import com.makaota.weathermzansi.utils.Resource
import com.makaota.weathermzansi.weather.DailyWeatherData
import com.makaota.weathermzansi.weather.DailyWeatherInfo
import com.makaota.weathermzansi.weather.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CombinedWeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {

    var state by mutableStateOf(WeatherState())
        private set

    var dailyWeatherState by mutableStateOf(DailyWeatherState())
        private set

    private val weatherState = MutableStateFlow(WeatherState())
    val _weatherState: StateFlow<WeatherState> = weatherState


    // Add an isRefreshing state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing



    private val _selectedLocation = MutableStateFlow<String?>("Unknown Location")
    val selectedLocation: StateFlow<String?> = _selectedLocation

    private val _latLng = MutableStateFlow<LatLng?>(null)



    var selectedWeatherData by mutableStateOf<DailyWeatherData?>(null)
        private set

    fun selectWeatherData(data: DailyWeatherData) {
        selectedWeatherData = data
    }
    var selectedDayIndex by mutableStateOf(0) // Default to first day
        private set

    fun selectDay(index: Int) {
        selectedDayIndex = index
    }
    fun getSelectedDay(): Int {
        return selectedDayIndex
    }
    fun setError(message: String) {
        state = state.copy(
            isLoading = false,
            error = message
        )
    }



    fun loadWeatherData() {
        viewModelScope.launch {
            _isRefreshing.value = true

            state = state.copy(isLoading = true, error = null)
            dailyWeatherState = dailyWeatherState.copy(isLoading = true, error = null)
            val location = locationTracker.getCurrentLocation()
            try {
                

                if (location == null) {
                    val errorMsg = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                    state = state.copy(isLoading = false, error = errorMsg)
                    dailyWeatherState = dailyWeatherState.copy(isLoading = false, error = errorMsg)
                    _isRefreshing.value = false
                    return@launch
                }

                val current = async {
                    try {
                        repository.getWeatherData(location.latitude, location.longitude)
                    } catch (e: Exception) {
                        Log.e("WeatherVM", "Error getting current weather", e)
                        throw Exception("Failed to load current weather. Check your internet connection.")
                    }
                }

                val daily = async {
                    try {
                        repository.getDailyWeatherData(location.latitude, location.longitude)
                    } catch (e: Exception) {
                        Log.e("WeatherVM", "Error getting daily weather", e)
                        throw Exception("Failed to load daily weather. Check your internet connection.")
                    }
                }

                try {
                    val currentResult = current.await()
                    handleCurrentWeatherResult(currentResult)
                } catch (e: Exception) {
                    state = state.copy(isLoading = false, error = e.message)
                }

                try {
                    val dailyResult = daily.await()
                    handleDailyWeatherResult(dailyResult)
                } catch (e: Exception) {
                    dailyWeatherState = dailyWeatherState.copy(isLoading = false, error = e.message)
                }

            } catch (e: Exception) {
                Log.e("WeatherVM", "Unhandled error", e)
                val errorMsg = "Something went wrong. Check internet and try again."
                state = state.copy(isLoading = false, error = errorMsg)
                dailyWeatherState = dailyWeatherState.copy(isLoading = false, error = errorMsg)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun handleCurrentWeatherResult(result: Resource<WeatherInfo>) {
        when (result) {
            is Resource.Success -> {
                state = state.copy(
                    weatherInfo = result.data,
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                state = state.copy(
                    isLoading = false,
                    error = result.message ?: "An unknown error occurred."
                )
            }
            else->{}
        }
    }

    private fun handleDailyWeatherResult(result: Resource<DailyWeatherInfo>) {
        when (result) {
            is Resource.Success -> {
                dailyWeatherState = dailyWeatherState.copy(
                    dailyWeatherInfo = result.data,
                    isLoading = false,
                    error = null
                )
            }
            is Resource.Error -> {
                dailyWeatherState = dailyWeatherState.copy(
                    isLoading = false,
                    error = result.message ?: "An unknown error occurred."
                )
            }
            else->{}
        }
    }

    fun updateLocation(city: String?, coordinates: LatLng?) {
        _selectedLocation.value = city
        _latLng.value = coordinates
    }

    fun loadWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            fetchWeather(lat, lon)
        }
    }

    fun fetchWeather(lat: Double, long: Double) {
        viewModelScope.launch {
            _isRefreshing.value = true
            state = state.copy(isLoading = true, error = null)
            dailyWeatherState = dailyWeatherState.copy(isLoading = true, error = null)

            try {
                val result = repository.fetchCombinedWeatherData(lat, long)

                when (result) {
                    is Resource.Success -> {
                        val (weatherInfo, dailyWeatherInfo) = result.data!!
                        state = state.copy(weatherInfo = weatherInfo, isLoading = false, error = null)
                        dailyWeatherState = dailyWeatherState.copy(dailyWeatherInfo = dailyWeatherInfo, isLoading = false, error = null)
                    }
                    is Resource.Error -> {
                        state = state.copy(isLoading = false, error = result.message ?: "An unknown error occurred.")
                        dailyWeatherState = dailyWeatherState.copy(isLoading = false, error = result.message ?: "An unknown error occurred.")
                    }
                }
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = "Failed to load weather: ${e.message}")
                dailyWeatherState = dailyWeatherState.copy(isLoading = false, error = "Failed to load weather: ${e.message}")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

}


