package com.example.olegweatherapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.models.onecall.ForecastOnecall
import com.example.olegweatherapp.repository.HomeRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * HomeViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during actiivty
 * or fragment lifecycle events.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val homeRepository = HomeRepository(Injection.provideDatabase(application))

    /**
     * forecast displayed on the screen.
     */
    val forecastOnecall = homeRepository.forecastOnecall

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

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
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

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
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                homeRepository.refreshForecastOnecall()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                //Show a Toast error message
                if (forecastOnecall.value == null) {
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



    private val _text = MutableLiveData<String>()
    val text: LiveData<String>
        get() = _text

    private fun getWeatherByLocation () {
        viewModelScope.launch {
            try {
                val forecastOnecall = Injection.provideNetworkApi()
                    .getByCoordinates(53.895487, 27.559835)
                val gson = Gson()
                //from object to Json string
                val string : String = gson.toJson(forecastOnecall)
                //from Json string to object
                val newForecastonecall = gson.fromJson(string, ForecastOnecall::class.java)
                if (forecastOnecall == newForecastonecall) {
                    _text.value = newForecastonecall.toString()
                }
            } catch (e: Exception) {
                _text.value = "Failure: ${e.message}"
            }
        }
    }


}