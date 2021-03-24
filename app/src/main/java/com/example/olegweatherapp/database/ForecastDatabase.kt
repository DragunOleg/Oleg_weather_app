package com.example.olegweatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseForecastOnecall::class,DatabaseForecastCity::class], version = 1)
abstract class ForecastDatabase: RoomDatabase() {
    abstract val forecastOnecallDao: ForecastOnecallDao
}