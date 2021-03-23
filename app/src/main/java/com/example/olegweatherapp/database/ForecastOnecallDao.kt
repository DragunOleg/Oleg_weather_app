package com.example.olegweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * D
 */
@Dao
interface ForecastOnecallDao {
    @Query("select * from databaseforecastonecall")
    fun getOnecall(): LiveData<DatabaseForecastOnecall>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(forecastOnecall: DatabaseForecastOnecall)
}