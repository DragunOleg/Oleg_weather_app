package com.example.olegweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One entity of our database object
 */
@Entity
data class DatabaseForecastOnecall constructor(
    @PrimaryKey
    val dt: Int,
    val forecastOnecall: String)

