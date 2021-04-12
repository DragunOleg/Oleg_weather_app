package com.example.olegweatherapp.di

import android.content.Context
import com.example.olegweatherapp.data.ForecastDao
import com.example.olegweatherapp.data.ForecastDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideForecastDatabase(@ApplicationContext context: Context): ForecastDatabase {
        return ForecastDatabase.getInstance(context)
    }

    @Provides
    fun provideForecastDao(forecastDatabase: ForecastDatabase): ForecastDao {
        return forecastDatabase.forecastDao()
    }
}