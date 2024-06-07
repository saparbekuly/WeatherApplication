package com.application.weather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    val country: String,
    val state: String? = null
)