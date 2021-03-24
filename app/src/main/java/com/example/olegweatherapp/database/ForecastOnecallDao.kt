package com.example.olegweatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Dao for both tables
 */
@Dao
interface ForecastOnecallDao {
    /**
     * Onecall methods
     */
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

    /**
     * Cities methods
     */
    @Query("select * from databaseforecastcity")
    fun getAllCities(): LiveData<List<DatabaseForecastCity>>

    //transaction make sure BOTH operations done
    @Transaction
    fun updateCitiesData(cities: List<DatabaseForecastCity>) {
        deleteAllCities()
        insertAllCities(cities)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: DatabaseForecastCity)

    @Delete
    fun deleteCity(city: DatabaseForecastCity)


    @Insert
    fun insertAllCities(cities: List<DatabaseForecastCity>)

    @Query("DELETE FROM databaseforecastcity")
    fun deleteAllCities()
}