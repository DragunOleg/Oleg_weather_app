package com.example.olegweatherapp.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.olegweatherapp.repository.FavoritesRepository
import com.example.olegweatherapp.repository.HomeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import timber.log.Timber

//it is not coroutineWorker cause of trouble with HiltWorker
@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val homeRepository: HomeRepository,
    private val favoritesRepository: FavoritesRepository
) :
    Worker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.olegweatherapp.work.RefreshDataWorker"
    }

    override fun doWork(): Result {
        Timber.d("forecast: doWork called")
        try {
            val sharedPref =
                appContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val scale = sharedPref.getInt("scale", 1)
            val lat = sharedPref.getFloat("latitude", (40.462212).toFloat()).toDouble()
            val lon = sharedPref.getFloat("longitude", (-2.96039).toFloat()).toDouble()
            runBlocking {
                homeRepository.refreshForecastOnecall(Pair(lat, lon), scale)
                favoritesRepository.refreshForecastCities(scale)
            }
            Timber.d("forecast: WorkManager: dowork end of try")
        } catch (e: Exception) {
            Timber.d("forecast: WorkManager e: $e")
            return Result.retry()
        }
        Timber.d("Forecast WorkManager success")
        return Result.success()
    }
}