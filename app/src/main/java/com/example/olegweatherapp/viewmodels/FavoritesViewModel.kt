package com.example.olegweatherapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.repository.FavoritesRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * FavoritesViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val favoritesRepository = FavoritesRepository(Injection.provideDatabase(application))

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
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        refreshDataFromRepository()
        Timber.d("forecast: FavoritesViewModel init")
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                favoritesRepository.refreshForecastCities()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                //Show a Toast error message
                if(citiesList.value == null) {
                    _eventNetworkError.value = true
                }
            }
        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }




//    private val _text = MutableLiveData<String>().apply {
//        value = "This is favorites Fragment"
//    }
//    val text: LiveData<String> = _text
//
//    init {
//        getWeatherByCityName()
//    }
//
//    private fun getWeatherByCityName () {
//        viewModelScope.launch {
//            try {
//                val forecastByCity = Injection.provideNetworkApi()
//                    .getByCityName("Moscow")
//                _text.value = forecastByCity.toString()
//            } catch (e: Exception) {
//                _text.value = "Failure: ${e.message}"
//            }
//        }
//    }
}