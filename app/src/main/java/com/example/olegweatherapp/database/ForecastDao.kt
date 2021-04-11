package com.example.olegweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Dao for both tables
 */
@Dao
interface ForecastDao {
    /**
     * Onecall methods******************************************
     */

    //it is always single instance because update recreate it
    @Query("select * from databaseforecastonecall")
    fun getOnecall(): LiveData<DatabaseForecastOnecall>

    //transaction make sure BOTH operations done
    @Transaction
    fun updateData(forecastOnecall: DatabaseForecastOnecall) {
        deleteAllForecastOnecall()
        insertAll(forecastOnecall)
    }

    //don't call this from repo
    @Insert
    fun insertAll(forecastOnecall: DatabaseForecastOnecall)

    //don't call this from repo
    @Query("DELETE FROM databaseforecastonecall")
    fun deleteAllForecastOnecall()

    /**
     * Cities methods*********************************************
     */

    @Query("select * from databaseforecastcity")
    fun getAllCities(): LiveData<List<DatabaseForecastCity>>

    @Query("select cityId FROM databaseforecastcity")
    fun getCitiesNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCities(list: List<DatabaseForecastCity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: DatabaseForecastCity)

    @Query("DELETE FROM databaseforecastcity WHERE cityId = :cityID")
    fun deleteCity(cityID: String)
}