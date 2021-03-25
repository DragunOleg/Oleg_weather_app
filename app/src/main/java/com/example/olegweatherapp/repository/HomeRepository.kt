package com.example.olegweatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.database.DatabaseForecastOnecall
import com.example.olegweatherapp.database.ForecastDatabase
import com.example.olegweatherapp.models.onecall.ForecastOnecall
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class HomeRepository (private val database: ForecastDatabase) {

    private val gson = Gson()

    /**
     * Observable object of home weather. Show this in your UI
     */
    val forecastOnecall: LiveData<ForecastOnecall> =
        Transformations.map(database.forecastOnecallDao.getOnecall()) {
            it?.asDomainModel()
        }

    suspend fun refreshForecastOnecall(){
        withContext(Dispatchers.IO) {
            Timber.d("forecast: refresh home is called")
            try {
                val forecastOnecall =
                        Injection.provideNetworkApi().getByCoordinates(53.895487, 27.559835)
                database.forecastOnecallDao.updateData(forecastOnecall.asDatabaseModel())
            } catch (e: Exception) {
                throw IOException()
            }
        }
    }


    /**
     * Transform domain object to database object
     */
    private fun ForecastOnecall.asDatabaseModel() : DatabaseForecastOnecall {
        //from object to Json string
        return DatabaseForecastOnecall(
            dt = this.current.dt,
            forecastOnecall = gson.toJson(this)
        )
    }

    /**
     * Transform database object to domain object
     */
    private fun DatabaseForecastOnecall.asDomainModel() : ForecastOnecall {
        //from Json string to object
        return gson.fromJson(this.forecastOnecall, ForecastOnecall::class.java) as ForecastOnecall
    }
}