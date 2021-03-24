package com.example.olegweatherapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseForecastCity(
        @PrimaryKey
        val cityId: String,
        val forecastOnecall: String)
