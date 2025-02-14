package com.makaota.weathermzansi

import android.app.Application
import androidx.room.Room
import com.makaota.weathermzansi.data.location_database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherMzansiApp: Application(){
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "weather_db"
        ).build()
    }
}