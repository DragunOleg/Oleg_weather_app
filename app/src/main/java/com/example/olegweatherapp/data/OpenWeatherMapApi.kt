package com.example.olegweatherapp.data

import com.example.olegweatherapp.models.bycityname.ForecastByCity
import com.example.olegweatherapp.models.onecall.ForecastOnecall
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * models are in models package
 */
interface OpenWeatherMapApi {

    @GET("data/2.5/onecall")
    suspend fun getByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = APP_ID,
        @Query("units") units: String = "metric"
    ): ForecastOnecall

    /**
     * @param city might be City, City + state code, City + state + country divided by comma
     * more: https://openweathermap.org/current#name
     */
    @GET("data/2.5/weather")
    suspend fun getByCityName(
        @Query("q") city: String,
        @Query("appid") appId: String = APP_ID,
        @Query("units") units: String = "metric"
    ): ForecastByCity


    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/"
        private const val APP_ID = "5c7190d55851716b076fb3da74fcf737"

        fun create(): OpenWeatherMapApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapApi::class.java)
        }
    }
}