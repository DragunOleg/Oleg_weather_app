package com.example.olegweatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String>
        get() = _text

    init {
        getWeatherByLocation()
    }

    private fun getWeatherByLocation () {
        viewModelScope.launch {
            try {
                val forecastOnecall = Injection.provideNetworkApi()
                    .getByCoordinates(53.895487, 27.559835)
                _text.value = forecastOnecall.current.toString()
            } catch (e: Exception) {
                _text.value = "Failure: ${e.message}"
            }
        }
    }
}