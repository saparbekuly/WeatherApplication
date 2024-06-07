package com.application.weather.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.application.weather.R
import com.application.weather.ui.WeatherViewModel
import com.application.weather.data.remote.models.City
import com.application.weather.ui.theme.BackGround
import com.application.weather.ui.theme.Gray
import com.application.weather.ui.theme.Green
import com.application.weather.ui.theme.Yellow
import com.application.weather.utils.isInternetAvailable

@Composable
fun SearchScreen(navController: NavHostController, viewModel: WeatherViewModel) {
    var text by remember { mutableStateOf("") }
    val cityList by viewModel.cityList.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    val isOnline = remember { mutableStateOf(true) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGround),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.navigate("location")
                    viewModel.clearCityList()
                }
            ) {
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
                    .padding(end = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Search Location",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(52.dp),
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "Enter your city", color = Gray, fontSize = 15.sp) },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Green,
                focusedBorderColor = Green,
                unfocusedBorderColor = Gray,
            ),
            singleLine = true,
        )


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if (isInternetAvailable(context)) {
                        viewModel.searchCity(text)
                        isOnline.value = true
                    } else {
                        isOnline.value = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (text.isEmpty() && text.isBlank()) Gray else Yellow,
                    contentColor = if (text.isEmpty() && text.isBlank()) Gray else Color.Black
                ),
                enabled = text.isNotEmpty() && text.isNotBlank()
            ) {
                Text(
                    text = "Search"
                )
            }
        }

        Spacer(modifier = Modifier.height(7.dp))

        if (!isOnline.value) {
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

        if (isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                items(5) {
                    LoadingBar()
                    Spacer(modifier = Modifier.height(5.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(cityList) { city ->
                    SearchCityItem(city) {
                        viewModel.addCity(city)
                        viewModel.clearCityList()
                        navController.navigate("location")
                    }
                }
            }
        }
    }
}


@Composable
fun SearchCityItem(city: City, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 5.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = city.name,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = city.state ?: "",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Text(
                text = city.country,
                style = TextStyle(fontSize = 16.sp, color = Color.Black)
            )
        }
    }
}


@Composable
private fun LoadingBar() {
    Box(
        modifier = Modifier
            .padding(top = 2.dp, bottom = 6.dp)
            .shadow(5.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(color = Color.LightGray)
                    .height(20.dp)
                    .fillMaxWidth()
                    .shimmerLoadingAnimation()
            )
        }
    }
}

fun Modifier.shimmerLoadingAnimation(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000
): Modifier {
    return composed {

        val shimmerColors = listOf(
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 1.0f),
            Color.White.copy(alpha = 0.5f),
            Color.White.copy(alpha = 0.3f),
        )

        val transition = rememberInfiniteTransition(label = "")

        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            ),
        )
    }
}