package com.makaota.weathermzansi.di

import com.makaota.weathermzansi.data.location.DefaultLocationTracker
import com.makaota.weathermzansi.data.repository.WeatherRepositoryImpl
import com.makaota.weathermzansi.domain.location.LocationTracker
import com.makaota.weathermzansi.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}