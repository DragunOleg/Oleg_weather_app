package com.example.olegweatherapp.models.bycityname

import com.example.olegweatherapp.models.Weather
import com.google.gson.annotations.SerializedName
import java.time.format.DateTimeFormatter

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

/**
 * Main data class for call by City name (also possible to get by city ID, Zip, coord)
 * https://openweathermap.org/current#parameter
 */
data class ForecastByCity (

		@SerializedName("coord") val coord : Coord,
		@SerializedName("weather") val weather : List<Weather>,
		@SerializedName("base") val base : String,
		@SerializedName("main") val main : Main,
		@SerializedName("visibility") val visibility : Double,
		@SerializedName("wind") val wind : Wind,
		@SerializedName("clouds") val clouds : Clouds,
		@SerializedName("dt") val dt : Int,
		@SerializedName("sys") val sys : Sys,
		@SerializedName("timezone") val timezone : Int,
		@SerializedName("id") val id : Int,
		@SerializedName("name") val name : String,
		//error gonna be here
		@SerializedName("cod") val cod : Int
) {
	// TODO: change string for lower devices
	val dateTime: String
	get() = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
		"calculated at local\n" + DateTimeFormatter.ISO_INSTANT
			.format(java.time.Instant.ofEpochSecond((dt+timezone).toLong()))
				.replace('T',' ')
				.dropLast(1)

	} else {
		""
	}

}