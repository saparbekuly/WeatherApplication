package com.application.weather.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.weather.ui.WeatherViewModel
import com.application.weather.ui.screens.LocationScreen
import com.application.weather.ui.screens.SearchScreen
import com.application.weather.ui.screens.WeatherScreen

@Composable
fun WeatherApp(navController: NavHostController, viewModel: WeatherViewModel) {
    NavHost(navController = navController, startDestination = "location") {
        composable("location") { LocationScreen(navController, viewModel) }
        composable("search") { SearchScreen(navController, viewModel) }
        composable(
            "weather/{lat}/{lon}",
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType },
                navArgument("lon") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
            WeatherScreen(navController, lat, lon)
        }
    }
}