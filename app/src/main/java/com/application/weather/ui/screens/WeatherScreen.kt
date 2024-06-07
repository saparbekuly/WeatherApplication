package com.application.weather.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.application.weather.R
import com.application.weather.ui.WeatherViewModel
import com.application.weather.ui.theme.BackGround
import com.application.weather.ui.theme.Green
import com.application.weather.utils.isInternetAvailable
import org.koin.androidx.compose.getViewModel

@Composable
fun WeatherScreen(navController: NavHostController, lat: Double, lon: Double) {
    val viewModel: WeatherViewModel = getViewModel()
    val weatherDetails by viewModel.weatherDetails.collectAsState()
    val context = LocalContext.current
    val isOnline = remember { mutableStateOf(false) }

    if (isInternetAvailable(context)) {
        isOnline.value = true
        viewModel.fetchWeatherDetails(lat, lon)
    } else {
        isOnline.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate("location") }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Go Back",
                    tint = Green
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 53.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Weather today",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        if (isOnline.value) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                Column {
                    weatherDetails?.let { details ->
                        Text("City: ${details.name}", fontSize = 16.sp)
                        Text("Weather: ${details.weather[0].main}", fontSize = 16.sp)
                        Text(
                            "Weather description: ${details.weather[0].description}",
                            fontSize = 16.sp
                        )
                        Text("Temperature: ${details.main.temp}°C", fontSize = 16.sp)
                        Text(
                            "Temperature feels like: ${details.main.feels_like}°C", fontSize = 16.sp
                        )
                        Text("Max temperature: ${details.main.temp_max}°C", fontSize = 16.sp)
                        Text("Min temperature: ${details.main.temp_min}°C", fontSize = 16.sp)
                        Text("Humidity: ${details.main.humidity} %", fontSize = 16.sp)
                        Text("Sea level: ${details.main.sea_level} hPa", fontSize = 16.sp)
                    } ?: run {
                        Text("Loading...")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_empty),
                    contentDescription = "No locations",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

    }
}