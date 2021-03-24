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

class FavoritesRepository (private val database: ForecastDatabase) {

    private val gson = Gson()

    val cities: LiveData<List<ForecastByCity>> =
            Transformations.map(database.forecastOnecallDao.getAllCities()){
                it?.asDomainModel()
            }

    suspend fun refreshForecastCities(){
        withContext(Dispatchers.IO) {
            Timber.d("forecast: refresh favorites is called")
            val tempCities =
                    Injection.provideNetworkApi().getByCityName("Moscow")
            database.forecastOnecallDao.updateCitiesData(listOf(tempCities).asDatabaseModel())
        }
    }

    //suspend fun insertCity(name: String) {}

    //suspend fun deleteCity(name: String) {}

    //suspend fun deleteAllCities() {}

    private fun List<ForecastByCity>.asDatabaseModel(): List<DatabaseForecastCity> {
        //from object to Json string
        return map {
            DatabaseForecastCity(
                    cityId = it.name,
                    forecastCity = gson.toJson(it)
            )
        }
    }

    private fun List<DatabaseForecastCity>.asDomainModel() : List<ForecastByCity> {
        //from Json string to object
        return map {
            gson.fromJson(it.forecastCity, ForecastByCity::class.java) as ForecastByCity
        }
    }
}
