package com.example.olegweatherapp.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.olegweatherapp.Injection
import com.example.olegweatherapp.database.DatabaseForecastCity
import com.example.olegweatherapp.database.ForecastDatabase
import com.example.olegweatherapp.models.bycityname.ForecastByCity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

class FavoritesRepository (private val database: ForecastDatabase) {

    private val gson = Gson()

    /**
     * Observable list of favorite cities. Show this in your UI
     */
    val cities: LiveData<List<ForecastByCity>> =
            Transformations.map(database.forecastOnecallDao.getAllCities()){
                it?.asDomainModel()
            }

    suspend fun refreshForecastCities(){
        withContext(Dispatchers.IO) {
            Timber.d("forecast: refresh favorites is called")
            try {
                val tempCities = Injection.provideNetworkApi().getByCityName("sdfkhjsdaf")
                if (tempCities.cod == 200) {
                    database.forecastOnecallDao.updateCitiesData(listOf(tempCities).asDatabaseModel())
                }
            } catch (e: Exception) {
                throw IOException()
            }
        }
    }

    //suspend fun insertCity(name: String) {}

    //suspend fun deleteCity(name: String) {}

    //suspend fun deleteAllCities() {}

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
     * Transform database list to domain list
     */
    private fun List<DatabaseForecastCity>.asDomainModel() : List<ForecastByCity> {
        //from Json string to object
        return map {
            gson.fromJson(it.forecastCity, ForecastByCity::class.java) as ForecastByCity
        }
    }
}
