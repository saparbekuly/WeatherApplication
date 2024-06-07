package com.application.weather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.weather.data.local.CityEntity
import com.application.weather.data.remote.models.City
import com.application.weather.data.remote.models.WeatherDetails
import com.application.weather.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _cityList = MutableStateFlow<List<City>>(emptyList())
    val cityList: StateFlow<List<City>> get() = _cityList

    private val _selectedCities = MutableStateFlow<List<CityEntity>>(emptyList())
    val selectedCities: StateFlow<List<CityEntity>> get() = _selectedCities

    private val _weatherDetails = MutableStateFlow<WeatherDetails?>(null)
    val weatherDetails: StateFlow<WeatherDetails?> get() = _weatherDetails

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading

    init {
        viewModelScope.launch {
            repository.getSelectedCities().collect { cities ->
                _selectedCities.value = cities
            }
        }
    }

    fun searchCity(cityName: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val result = repository.getCityList(cityName)
                _cityList.value = result
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching city list", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun fetchWeatherDetails(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val weatherDetails = repository.getWeatherDetails(lat, lon)
                _weatherDetails.value = weatherDetails
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather details", e)
            }
        }
    }


    fun addCity(city: City) {
        val cityEntity = CityEntity(
            id = "${city.lat}_${city.lon}",
            lat = city.lat,
            lon = city.lon,
            name = city.name,
            country = city.country,
            state = city.state.orEmpty()
        )
        viewModelScope.launch {
            repository.addCity(cityEntity)
        }
    }

    fun deleteCity(city: CityEntity) {
        viewModelScope.launch {
            repository.removeCity(city)
        }
    }


    fun clearCityList() {
        _cityList.value = emptyList()
    }
}