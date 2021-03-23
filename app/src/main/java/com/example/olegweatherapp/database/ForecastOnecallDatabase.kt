package com.example.olegweatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseForecastOnecall::class], version = 1)
abstract class ForecastOnecallDatabase: RoomDatabase() {
    abstract val forecastOnecallDao: ForecastOnecallDao
}