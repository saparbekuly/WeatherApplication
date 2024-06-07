package com.application.weather.data.remote.models

data class WeatherDetails(
    val name: String,
    val weather: List<Weather>,
    val main: Main
)


