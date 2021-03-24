package com.example.olegweatherapp

import FavoritesRepository
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.olegweatherapp.database.ForecastDatabase
import com.example.olegweatherapp.network.OpenWeatherMapApi
import com.example.olegweatherapp.repository.SettingsRepository
import com.example.olegweatherapp.viewmodels.factories.FavoritesViewModelFactory
import com.example.olegweatherapp.viewmodels.factories.SettingsViewModelFactory
import timber.log.Timber

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {
    private val networkApi = OpenWeatherMapApi.create()
    private lateinit var  databaseINSTANCE : ForecastDatabase


    private fun provideFavoritesRepository(): FavoritesRepository {
        return FavoritesRepository()
    }

    private fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepository()
    }
    /**
     * provide singleton network Api
     */
    fun provideNetworkApi(): OpenWeatherMapApi {
        return networkApi
    }
    fun provideDatabase(context: Context) : ForecastDatabase {
        synchronized(ForecastDatabase::class.java) {
            if(!::databaseINSTANCE.isInitialized) {
                databaseINSTANCE = Room.databaseBuilder(context.applicationContext,
                ForecastDatabase::class.java,
                "databaseforecast").build()
                Timber.d("forecast: Database initialized")
            }
        }
        return databaseINSTANCE
    }

    fun provideFavoritesViewModelFactory(): ViewModelProvider.Factory {
        return FavoritesViewModelFactory(provideFavoritesRepository())
    }

    fun provideSettingsViewModelFactory(): ViewModelProvider.Factory {
        return SettingsViewModelFactory(provideSettingsRepository())
    }
}