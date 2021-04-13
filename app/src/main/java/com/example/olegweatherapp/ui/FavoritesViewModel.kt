package com.example.olegweatherapp.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.olegweatherapp.repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * FavoritesViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param app The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
        private val favoritesRepository: FavoritesRepository,
        private val app: Application
) : AndroidViewModel(app) {

    //cities displayed on the screen
    val citiesList = favoritesRepository.cities

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                favoritesRepository.refreshForecastCities(getScaleFromPref())
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (e: Exception) {
                Timber.d("forecast: refresh fav exception $e")
                //Show a Toast error message
                if (citiesList.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }

    private fun privateAddCity(city: String) {
        viewModelScope.launch {
            try {
                favoritesRepository.insertCity(city, getScaleFromPref())
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (e: Exception) {
                Timber.d("forecast: add city $city exception $e")
                //Show a Toast error message
                _eventNetworkError.value = true
            }
        }
    }

    private fun getScaleFromPref(): Int {
        val sharedPref = app.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPref.getInt("scale", 1)
    }

    /**
     * add new city
     */
    fun addCity(city: String) {
        privateAddCity(city)
    }

    private fun privateDeleteCity(city: String) {
        viewModelScope.launch {
            try {
                favoritesRepository.deleteCity(city)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (e: Exception) {
                Timber.d("forecast: delete city exception $e")
                //Show a Toast error message
                _eventNetworkError.value = true
            }
        }
    }

    /**
     * delete existing city
     * @param city name of the city. It is in [citiesList.value[0].name]
     */
    fun deleteCity(city: String) {
        privateDeleteCity(city)
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
}