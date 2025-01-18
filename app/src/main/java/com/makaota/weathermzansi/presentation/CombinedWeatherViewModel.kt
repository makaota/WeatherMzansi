package com.makaota.weathermzansi.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.makaota.weathermzansi.domain.location.LocationTracker
import com.makaota.weathermzansi.domain.repository.WeatherRepository
import com.makaota.weathermzansi.utils.Resource
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

    private val _state = MutableStateFlow(WeatherState())
    val refreshState: StateFlow<WeatherState> = _state

    // Add an isRefreshing state
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun loadWeatherData() {
        viewModelScope.launch {

            _isRefreshing.value = true
            try {
                // Load weather data here
                state = state.copy(isLoading = true, error = null)
                dailyWeatherState = dailyWeatherState.copy(isLoading = true, error = null)

                val location = locationTracker.getCurrentLocation()
                if (location == null) {
                    state = state.copy(
                        isLoading = false,
                        error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                    )
                    dailyWeatherState = dailyWeatherState.copy(
                        isLoading = false,
                        error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                    )
                    return@launch
                }

                val currentWeatherDeferred =
                    async { repository.getWeatherData(location.latitude, location.longitude) }
                val dailyWeatherDeferred =
                    async { repository.getDailyWeatherData(location.latitude, location.longitude) }

                val currentWeatherResult = currentWeatherDeferred.await()
                val dailyWeatherResult = dailyWeatherDeferred.await()

                handleCurrentWeatherResult(currentWeatherResult)
                handleDailyWeatherResult(dailyWeatherResult)
            } catch (e: Exception) {
                // Handle errors

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
}
