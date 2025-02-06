package com.makaota.weathermzansi.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.makaota.weathermzansi.domain.location.LocationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LocationTrackerImpl(private val context: Context) : LocationTracker {
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location? {
        return withContext(Dispatchers.IO) {
            val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location = fusedLocationProvider.lastLocation.await()
                location
            } catch (e: Exception) {
                null
            }
        }
    }
}