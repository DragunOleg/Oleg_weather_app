package com.example.olegweatherapp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("data/2.5/onecall")
    fun getByCoordinates(
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
            @Query("appid") appId : String = APP_ID,
            //TODO make swap based on settings to metric/standard/imperial
            //https://openweathermap.org/api/one-call-api#data Units of measurement
            @Query("units") units : String = "metric"
    ): Call<String>

    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"
        private const val APP_ID = "5c7190d55851716b076fb3da74fcf737"

        fun create() : OpenWeatherMapApi {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build()
                    .create(OpenWeatherMapApi::class.java)
        }
    }
}