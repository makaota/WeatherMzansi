package com.makaota.weathermzansi.data.location_database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double
)

