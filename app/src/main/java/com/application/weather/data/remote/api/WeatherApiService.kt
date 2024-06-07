package com.application.weather.data.remote.api

import com.application.weather.data.remote.models.City
import com.application.weather.data.remote.models.WeatherDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("geo/1.0/direct")
    suspend fun getCityList(
        @Query("q") city: String,
        @Query("limit") limit: Int,
        @Query("appid") key: String
    ): List<City>

    @GET("data/2.5/weather")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") key: String,
        @Query("units") units: String
    ): WeatherDetails
}