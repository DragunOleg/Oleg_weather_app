package com.example.olegweatherapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
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
    //TODO() migrate work to hilt
    override suspend fun doWork(): Result {
        Timber.d("forecast: doWork called")
        try {
            //shame on me. Should move app/appContext to Injection. So it would be almost
            //no boilerplate with shared pref and settings
            //val database = Injection.provideDatabase(applicationContext)
            //val homeRepository = HomeRepository(database)
            //val favoritesRepository = FavoritesRepository(database)
            val sharedPref =
                applicationContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val scale = sharedPref.getInt("scale", 1)
            val lat = sharedPref.getFloat("latitude", (40.462212).toFloat()).toDouble()
            val lon = sharedPref.getFloat("longitude", (-2.96039).toFloat()).toDouble()
            //homeRepository.refreshForecastOnecall(Pair(lat, lon), scale)
            //favoritesRepository.refreshForecastCities(scale)
            Timber.d("forecast: WorkManager: dowork end of try")
        } catch (e: Exception) {
            Timber.d("forecast: WorkManager e: $e")
            return Result.retry()
        }
        Timber.d("Forecast WorkManager success")
        return Result.success()
    }
}