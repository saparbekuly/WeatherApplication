package com.application.weather.di

import android.app.Application
import androidx.room.Room
import com.application.weather.ui.WeatherViewModel
import com.application.weather.data.local.CityDatabase
import com.application.weather.data.remote.api.WeatherApiService
import com.application.weather.data.repository.WeatherRepository
import com.application.weather.utils.Constants.BASE_URL
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    single {
        Room.databaseBuilder(get<Application>(), CityDatabase::class.java, "weather-database")
            .build()
    }

    single { get<CityDatabase>().cityDao() }
    single { WeatherRepository(get(), get()) }

    viewModel { WeatherViewModel(get()) }
}