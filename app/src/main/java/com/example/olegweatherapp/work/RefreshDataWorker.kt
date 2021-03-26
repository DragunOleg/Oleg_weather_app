package com.example.olegweatherapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.repository.FavoritesRepository
import com.example.olegweatherapp.repository.HomeRepository
import retrofit2.HttpException
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
        val database = Injection.provideDatabase(applicationContext)
        val homeRepository = HomeRepository(database)
        val favoritesRepository = FavoritesRepository(database)

        try {
            //todo
            //homeRepository.refreshForecastOnecall()
            favoritesRepository.refreshForecastCities()
            Timber.d("forecast: WorkManager: Work request for sync is run")
        } catch (e: HttpException) {
            return Result.retry()
        }
        return Result.success()
    }
}