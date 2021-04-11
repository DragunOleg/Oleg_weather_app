package com.example.olegweatherapp

import com.example.olegweatherapp.network.OpenWeatherMapApi

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */

object Injection {
    private val networkApi = OpenWeatherMapApi.create()

    /**
     * provide singleton network Api
     */
    fun provideNetworkApi(): OpenWeatherMapApi {
        return networkApi
    }
}