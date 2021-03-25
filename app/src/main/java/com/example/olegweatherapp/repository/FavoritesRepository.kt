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

    //TODO refresh database is never called
    suspend fun refreshForecastCities(){
        withContext(Dispatchers.IO) {
            Timber.d("forecast: refresh favorites is called")

            //if database cities is empty or null do nothing
            if(!cities.value.isNullOrEmpty()){
                //copy to be sure db change won't affect us when we work with network
                val citiesCopy = cities.value
                val citiesNames : List<String> = citiesCopy!!.map { it.name }
                //on each successful network call add valid db object
                val listToUpdate: List<DatabaseForecastCity> = listOf()

                citiesNames.forEach {
                    try {
                        val networkCity = Injection.provideNetworkApi().getByCityName(it)
                        //cod 200 = valid, add to list
                        if (networkCity.cod == 200) {
                            listToUpdate.plus(networkCity)
                        }
                    }catch (e: Exception) {
                        throw IOException()
                    }
                }
                //if all new objects are valid we add it to db, invalid won't be in list
                //otherwise do nothing
                Timber.d("forecast: list to update site ${listToUpdate.size} cities ${citiesNames.size}")
                if(listToUpdate.size == citiesNames.size) {
                    try {
                        Timber.d("forecast: updateCitiesData")
                        database.forecastOnecallDao.updateCitiesData(listToUpdate)
                    }catch (e:Exception) {
                        throw IOException()
                    }
                }
            }
        }
    }

    suspend fun insertCity (name: String) {
        withContext(Dispatchers.IO) {
            Timber.d("forecast: insert city with $name name")
            try {
                val networkCityToIncert = Injection.provideNetworkApi().getByCityName(name)
                if (networkCityToIncert.cod == 200) {
                    database.forecastOnecallDao.insertCity(networkCityToIncert.asDatabaseModel())
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
                database.forecastOnecallDao.deleteCity(name)
            } catch(e: Exception) {
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
    private fun ForecastByCity.asDatabaseModel() : DatabaseForecastCity {
        return DatabaseForecastCity(
                cityId = this.name,
                forecastCity = gson.toJson(this)
        )
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
