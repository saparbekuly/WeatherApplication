package com.application.weather.data.repository

import com.application.weather.data.remote.models.City
import com.application.weather.data.remote.api.WeatherApiService
import com.application.weather.data.remote.models.WeatherDetails
import com.application.weather.data.local.CityDao
import com.application.weather.data.local.CityEntity
import com.application.weather.utils.Constants.API_KEY
import com.application.weather.utils.Constants.LIMIT
import com.application.weather.utils.Constants.UNIT
import kotlinx.coroutines.flow.Flow


class WeatherRepository(private val apiService: WeatherApiService, private val cityDao: CityDao) {
    suspend fun getCityList(city: String): List<City> {
        return apiService.getCityList(city, LIMIT, API_KEY)
    }

    suspend fun getWeatherDetails(lat: Double, lon: Double): WeatherDetails {
        return apiService.getWeatherDetails(lat, lon, API_KEY, UNIT)
    }

    suspend fun addCity(city: CityEntity) {
        cityDao.insertCity(city)
    }

    suspend fun removeCity(city: CityEntity) {
        cityDao.deleteCity(city)
    }

    fun getSelectedCities(): Flow<List<CityEntity>> {
        return cityDao.getAllCities()
    }
}
