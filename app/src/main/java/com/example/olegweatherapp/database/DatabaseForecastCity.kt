package com.example.olegweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One entity for our city database object
 */
@Entity
data class DatabaseForecastCity(
        @PrimaryKey
        val cityId: String,
        val forecastCity: String)
