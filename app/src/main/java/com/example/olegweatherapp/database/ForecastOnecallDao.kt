package com.example.olegweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

/**
 * D
 */
@Dao
interface ForecastOnecallDao {
    @Query("select * from databaseforecastonecall")
    fun getOnecall(): LiveData<DatabaseForecastOnecall>

    @Transaction
    fun updateData(forecastOnecall: DatabaseForecastOnecall) {
        deleteAllForecastOnecall()
        insertAll(forecastOnecall)
    }

    @Insert
    fun insertAll(forecastOnecall: DatabaseForecastOnecall)

    @Query("DELETE FROM databaseforecastonecall")
    fun deleteAllForecastOnecall()
}