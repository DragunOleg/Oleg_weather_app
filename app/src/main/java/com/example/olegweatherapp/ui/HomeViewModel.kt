package com.example.olegweatherapp.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.olegweatherapp.repository.HomeRepository
import com.example.olegweatherapp.models.onecall.Daily
import com.example.olegweatherapp.models.onecall.Hourly
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

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
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val app: Application
) : AndroidViewModel(app) {

    /**
     * forecast displayed on the screen.
     */
    val forecastOnecall = homeRepository.forecastOnecall

    //hourly list to show on screen
    val hourlyList: LiveData<List<Hourly>> = Transformations.map(forecastOnecall) {
        it?.hourly
    }

    //daily list to show on screen
    val dailyList: LiveData<List<Daily>> = Transformations.map(forecastOnecall) {
        it?.daily
    }

    private var _sunrise: LiveData<String> = Transformations.map(forecastOnecall) {
        "sunrise \n" + dtToTime(it?.current?.sunrise)
    }
    val sunrise: LiveData<String>
        get() = _sunrise

    private var _sunset: LiveData<String> = Transformations.map(forecastOnecall) {
        "sunset \n" + dtToTime(it?.current?.sunset)
    }
    val sunset: LiveData<String>
        get() = _sunset

    private var _date: LiveData<String> = Transformations.map(forecastOnecall) {
        dtToDateTime(it?.current?.dt)
    }

    val date: LiveData<String>
        get() = _date

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
     * @param loc is pair with lat/lon to update weather with current location
     */
    fun refreshDataFromRepository() {

        viewModelScope.launch {
            val sharedPref = app.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val scale = sharedPref.getInt("scale", 1)
            val loc =
                if (sharedPref != null && sharedPref.contains("latitude") && sharedPref.contains("longitude")) {
                    val lat = sharedPref.getFloat("latitude", (40.462212).toFloat()).toDouble()
                    val lon = sharedPref.getFloat("longitude", (-2.96039).toFloat()).toDouble()
                    Pair(lat, lon)
                } else Pair(40.462212, -2.96039)
            try {
                homeRepository.refreshForecastOnecall(loc, scale)
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

    private fun dtToTime(utc: Int?): String {
        if (utc != null) {
            try {
                val sdf = SimpleDateFormat("HH:mm:ss")
                val netDate = Date(utc.toLong() * 1000)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }
        return ""
    }

    private fun dtToDateTime(utc: Int?): String {
        if (utc != null) {
            try {
                val sdf = SimpleDateFormat("MMM dd, yyyy\nHH:mm:ss", Locale.US)
                val netDate = Date(utc.toLong() * 1000)
                return sdf.format(netDate)
            } catch (e: Exception) {
                return e.toString()
            }
        }
        return ""
    }
}