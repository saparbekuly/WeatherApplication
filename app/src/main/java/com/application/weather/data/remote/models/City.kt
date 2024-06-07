package com.application.weather.data.remote.models

data class City(
    val lat: Double,
    val lon: Double,
    val name: String,
    val country: String,
    val state: String? = null
)