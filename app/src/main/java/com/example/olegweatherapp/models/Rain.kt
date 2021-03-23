package com.example.olegweatherapp.models

import com.google.gson.annotations.SerializedName

data class Rain(
        @SerializedName("1h") val oneHour : Double,
        @SerializedName("3h") val threeHour : Double
)
