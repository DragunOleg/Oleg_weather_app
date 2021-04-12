package com.example.olegweatherapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DatabaseForecastOnecall::class, DatabaseForecastCity::class], version = 1)
abstract class ForecastDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: ForecastDatabase? = null

        fun getInstance(context: Context): ForecastDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): ForecastDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ForecastDatabase::class.java,
                "databaseforecast"
            ).build()
        }
    }
}