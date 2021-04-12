package com.example.olegweatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.olegweatherapp.data.DatabaseForecastCity
import com.example.olegweatherapp.data.ForecastDao
import com.example.olegweatherapp.data.OpenWeatherMapApi
import com.example.olegweatherapp.models.bycityname.ForecastByCity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val forecastDao: ForecastDao,
    private val service: OpenWeatherMapApi
) {

    //TODO() provide with hilt
    private val gson = Gson()

    /**
     * Observable list of favorite cities. Show this in your UI
     */
    val cities: LiveData<List<ForecastByCity>> =
        Transformations.map(forecastDao.getAllCities()) {
            it?.asDomainModel()
        }

    suspend fun refreshForecastCities(scale: Int) {
        withContext(Dispatchers.IO) {
            Timber.d("forecast: refresh favorites is called")
            val db = forecastDao.getCitiesNames()
            val citiesToInsert = mutableListOf<ForecastByCity>()
            try {
                db.forEach {
                    citiesToInsert.add(
                        service.getByCityName(
                            it,
                            units = when (scale) {
                                1 -> "metric"
                                2 -> "standard"
                                3 -> "imperial"
                                else -> "metric"
                            }
                        )
                    )
                }
                forecastDao.insertAllCities(citiesToInsert.asDatabaseModel())
            } catch (e: Exception) {
                throw IOException()
            }
        }
    }

    suspend fun insertCity(name: String, scale: Int) {
        withContext(Dispatchers.IO) {
            Timber.d("forecast: insert city with $name name")
            try {
                val networkCityToIncert = service.getByCityName(
                    name,
                    units = when (scale) {
                        1 -> "metric"
                        2 -> "standard"
                        3 -> "imperial"
                        else -> "metric"
                    }
                )
                if (networkCityToIncert.cod == 200) {
                    forecastDao.insertCity(networkCityToIncert.asDatabaseModel())
                }
            } catch (e: Exception) {
                throw IOException()
            }
        }
    }

    suspend fun deleteCity(name: String) {
        withContext(Dispatchers.IO) {
            Timber.d("forecast: delete city with $name name")
            try {
                forecastDao.deleteCity(name)
            } catch (e: Exception) {
                throw IOException()
            }
        }
    }

    /**
     * Transform domain list to database list
     */
    private fun List<ForecastByCity>.asDatabaseModel(): List<DatabaseForecastCity> {
        //from object to Json string
        return map {
            DatabaseForecastCity(
                cityId = it.name,
                forecastCity = gson.toJson(it)
            )
        }
    }

    /**
     * Transform domain city to database city
     */
    private fun ForecastByCity.asDatabaseModel(): DatabaseForecastCity {
        return DatabaseForecastCity(
            cityId = this.name,
            forecastCity = gson.toJson(this)
        )
    }

    /**
     * Transform database list to domain list
     */
    private fun List<DatabaseForecastCity>.asDomainModel(): List<ForecastByCity> {
        //from Json string to object
        return map {
            gson.fromJson(it.forecastCity, ForecastByCity::class.java) as ForecastByCity
        }
    }
}
