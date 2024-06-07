package com.application.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.application.weather.ui.WeatherViewModel
import com.application.weather.utils.WeatherApp
import org.koin.androidx.compose.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: WeatherViewModel = getViewModel()
            val navController = rememberNavController()
            WeatherApp(navController, viewModel)
        }
    }
}