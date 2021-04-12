package com.example.olegweatherapp.di

import com.example.olegweatherapp.data.OpenWeatherMapApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapApli(): OpenWeatherMapApi {
        return OpenWeatherMapApi.create()
    }
}