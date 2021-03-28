package com.example.olegweatherapp

import android.content.Context
import androidx.room.Room
import com.example.olegweatherapp.database.ForecastDatabase
import com.example.olegweatherapp.network.OpenWeatherMapApi
import timber.log.Timber

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */

object Injection {
    private val networkApi = OpenWeatherMapApi.create()
    private lateinit var databaseINSTANCE: ForecastDatabase

    /**
     * provide singleton network Api
     */
    fun provideNetworkApi(): OpenWeatherMapApi {
        return networkApi
    }

    /**
     * provide singleton database instance
     */
    fun provideDatabase(context: Context): ForecastDatabase {
        synchronized(ForecastDatabase::class.java) {
            if (!::databaseINSTANCE.isInitialized) {
                databaseINSTANCE = Room.databaseBuilder(context.applicationContext,
                        ForecastDatabase::class.java,
                        "databaseforecast").build()
                Timber.d("forecast: Database initialized")
            }
        }
        return databaseINSTANCE
    }
}