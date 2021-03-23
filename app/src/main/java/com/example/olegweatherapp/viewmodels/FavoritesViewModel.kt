package com.example.olegweatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.repository.FavoritesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FavoritesRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favorites Fragment"
    }
    val text: LiveData<String> = _text

    init {
        getWeatherByCityName()
    }

    private fun getWeatherByCityName () {
        viewModelScope.launch {
            try {
                val forecastByCity = Injection.provideNetworkApi()
                    .getByCityName("Moscow")
                _text.value = forecastByCity.toString()
            } catch (e: Exception) {
                _text.value = "Failure: ${e.message}"
            }
        }
    }
}