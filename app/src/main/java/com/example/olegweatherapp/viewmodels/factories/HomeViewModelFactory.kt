package com.example.olegweatherapp.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.olegweatherapp.viewmodels.HomeViewModel

/**
 * Factory for constructing DevByteViewModel with parameter
 */
class HomeViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}