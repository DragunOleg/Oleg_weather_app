package com.example.olegweatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.olegweatherapp.network.OpenWeatherMapApi
import com.example.olegweatherapp.repository.HomeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    private val _text = MutableLiveData<String>()
    val text: LiveData<String>
        get() = _text

    init {
        getWeatherByLocation()
    }

    private fun getWeatherByLocation () {
        val apiObject = OpenWeatherMapApi.create()
        apiObject.getByCoordinates(53.895487, 27.559835).enqueue(
            object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _text.value = response.body()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _text.value = "Failure: " + t.message
                }

            }
        )
    }
}