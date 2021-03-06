package com.example.olegweatherapp.models.onecall

import com.example.olegweatherapp.models.Temp
import com.example.olegweatherapp.models.Weather
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Daily(

        @SerializedName("dt") val dt: Int,
        @SerializedName("sunrise") val sunrise: Double,
        @SerializedName("sunset") val sunset: Double,
        @SerializedName("temp") val temp: Temp,
        @SerializedName("feels_like") val feels_like: FeelsLike,
        @SerializedName("pressure") val pressure: Double,
        @SerializedName("humidity") val humidity: Double,
        @SerializedName("dew_point") val dewPoint: Double,
        @SerializedName("wind_speed") val windSpeed: Double,
        @SerializedName("wind_gust") val windGust: Double,
        @SerializedName("wind_deg") val windLeg: Double,
        @SerializedName("weather") val weather: List<Weather>,
        @SerializedName("clouds") val clouds: Double,
        @SerializedName("pop") val pop: Double,
        @SerializedName("snow") val snow: Double,
        @SerializedName("uvi") val uvi: Double
) {
    val formattedDt: String
        get() = dtToTime(dt)

    private fun dtToTime(utc: Int?): String {
        return if (utc != null) {
            try {
                val sdf = SimpleDateFormat("EEE", Locale.US)
                val netDate = Date(utc.toLong() * 1000)
                sdf.format(netDate)
            } catch (e: Exception) {
                e.toString()
            }
        } else ""
    }
}