package com.example.olegweatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.olegweatherapp.database.DatabaseForecastOnecall
import com.example.olegweatherapp.database.ForecastDao
import com.example.olegweatherapp.models.onecall.ForecastOnecall
import com.example.olegweatherapp.network.OpenWeatherMapApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepository @Inject constructor(
    private val forecastDao: ForecastDao,
    private val service: OpenWeatherMapApi
) {
    //TODO() provide with hilt
    private val gson = Gson()

    /**
     * Observable object of home weather. Show this in your UI
     */
    val forecastOnecall: LiveData<ForecastOnecall> =
        Transformations.map(forecastDao.getOnecall()) {
            it?.asDomainModel()
        }

    /**
     * @param loc is lat + lon
     * @param scale is int. 1 = metric, 2 = standard, 3 = imperial
     */
    suspend fun refreshForecastOnecall(loc: Pair<Double, Double>, scale: Int) {
        withContext(Dispatchers.IO) {
            Timber.d("forecast: refresh home is called. lat ${loc.first}, lon ${loc.second}")
            try {
                val scaleString = when (scale) {
                    1 -> "metric"
                    2 -> "standard"
                    3 -> "imperial"
                    else -> "metric"
                }
                val forecastOnecall =
                    service.getByCoordinates(loc.first, loc.second, units = scaleString)
                forecastDao.updateData(forecastOnecall.asDatabaseModel())
            } catch (e: Exception) {
                throw IOException()
            }
        }
    }


    /**
     * Transform domain object to database object
     */
    private fun ForecastOnecall.asDatabaseModel(): DatabaseForecastOnecall {
        //from object to Json string
        return DatabaseForecastOnecall(
            dt = this.current.dt,
            forecastOnecall = gson.toJson(this)
        )
    }

    /**
     * Transform database object to domain object
     */
    private fun DatabaseForecastOnecall.asDomainModel(): ForecastOnecall {
        //from Json string to object
        return gson.fromJson(this.forecastOnecall, ForecastOnecall::class.java) as ForecastOnecall
    }
}