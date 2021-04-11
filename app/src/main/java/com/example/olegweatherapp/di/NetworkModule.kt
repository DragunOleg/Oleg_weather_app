package com.example.olegweatherapp.di

import com.example.olegweatherapp.network.OpenWeatherMapApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//TODO add it to repos after DB
@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapApli(): OpenWeatherMapApi {
        return OpenWeatherMapApi.create()
    }
}