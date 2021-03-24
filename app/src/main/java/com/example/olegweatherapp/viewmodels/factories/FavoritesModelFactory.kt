package com.example.olegweatherapp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.olegweatherapp.viewmodels.FavoritesViewModel


/**
 * Factory for constructing FavoritesViewModel with parameter
 */
class FavoritesModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}
