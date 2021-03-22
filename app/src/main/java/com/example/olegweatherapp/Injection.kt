package com.example.olegweatherapp

import androidx.lifecycle.ViewModelProvider
import com.example.olegweatherapp.repository.FavoritesRepository
import com.example.olegweatherapp.repository.HomeRepository
import com.example.olegweatherapp.repository.SettingsRepository
import com.example.olegweatherapp.viewmodels.factories.FavoritesViewModelFactory
import com.example.olegweatherapp.viewmodels.factories.HomeViewModelFactory
import com.example.olegweatherapp.viewmodels.factories.SettingsViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideHomeRepository(): HomeRepository {
        return HomeRepository()
    }

    private fun provideFavoritesRepository(): FavoritesRepository {
        return FavoritesRepository()
    }

    private fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepository()
    }

    fun provideHomeViewModelFactory(): ViewModelProvider.Factory {
        return HomeViewModelFactory(provideHomeRepository())
    }

    fun provideFavoritesViewModelFactory(): ViewModelProvider.Factory {
        return FavoritesViewModelFactory(provideFavoritesRepository())
    }

    fun provideSettingsViewModelFactory(): ViewModelProvider.Factory {
        return SettingsViewModelFactory(provideSettingsRepository())
    }
}