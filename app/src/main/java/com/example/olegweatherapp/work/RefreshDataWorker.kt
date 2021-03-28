package com.example.olegweatherapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.extensions.getLocationWithoutActivity
import com.example.olegweatherapp.repository.FavoritesRepository
import com.example.olegweatherapp.repository.HomeRepository
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.olegweatherapp.work.RefreshDataWorker"
    }

    /**
     * The Android system gives a Worker a maximum of 10 minutes
     * to finish its execution and return a ListenableWorker.Result object
     */
    override suspend fun doWork(): Result {
        Timber.d("forecast: doWork called")
        try {
            val database = Injection.provideDatabase(applicationContext)
            val homeRepository = HomeRepository(database)
            val favoritesRepository = FavoritesRepository(database)
            val loc = getLocationWithoutActivity(applicationContext)
            homeRepository.refreshForecastOnecall(loc)
            favoritesRepository.refreshForecastCities()
            Timber.d("forecast: WorkManager: dowork end of try")
        } catch (e: Exception) {
            Timber.d("forecast: WorkManager e: $e")
            return Result.retry()
        }
        Timber.d("Forecast WorkManager end for trycatch")
        return Result.success()
    }
}