package com.example.olegweatherapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.database.DatabaseForecastOnecall
import com.example.olegweatherapp.database.ForecastOnecallDatabase
import com.example.olegweatherapp.models.onecall.ForecastOnecall
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepository (private val database: ForecastOnecallDatabase) {

    private val gson = Gson()

    val forecastOnecall : LiveData<ForecastOnecall> =
        Transformations.map(database.forecastOnecallDao.getOnecall()) {
            it.asDomainModel()
        }

    suspend fun refreshForecastOnecall(){
        withContext(Dispatchers.IO) {
            Log.d("forecast", "refresh home is called")
            val forecastOnecall =
                Injection.provideNetworkApi().getByCoordinates(53.895487, 27.559835)
            database.forecastOnecallDao.insertAll(forecastOnecall.asDatabaseModel())
        }
    }



    private fun ForecastOnecall.asDatabaseModel() : DatabaseForecastOnecall {
        //from object to Json string
        val string : String = gson.toJson(this)
        val dt = this.current.dt
        return DatabaseForecastOnecall(
            dt = dt,
            forecastOnecall = string
        )
    }

    private fun DatabaseForecastOnecall.asDomainModel() : ForecastOnecall {
        //from Json string to object
        return gson.fromJson(this.forecastOnecall, ForecastOnecall::class.java) as ForecastOnecall
    }
}