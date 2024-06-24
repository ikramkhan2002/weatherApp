package com.example.myapplication.android.weather

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun WeatherScreen() {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var cityName by remember { mutableStateOf<String?>(null) }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            location = getCurrentLocation(context)
            location?.let {
                cityName = getCityName(context, it.first, it.second)
                weather = RetrofitInstance.api.getWeather(it.first, it.second)
            }
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        cityName?.let {
            Text(text = it, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        when {
            error != null -> {
                Text(text = "Error: $error")
            }
            weather == null -> {
                CircularProgressIndicator()
            }
            else -> {
                weather?.let { weatherResponse ->
                    CurrentWeatherDisplay(weatherResponse.current)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Hourly Forecast")
                    HourlyWeatherDisplay(weatherResponse.hourly)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Daily Forecast")
                    DailyWeatherDisplay(weatherResponse.daily)
                }
            }
        }
    }
}

@Composable
fun CurrentWeatherDisplay(currentWeather: CurrentWeather) {
    val temperatureCelsius = (currentWeather.temp - 273.15).toInt()
    val feelsLikeCelsius = (currentWeather.feels_like - 273.15).toInt()
    val weatherDescription = currentWeather.weather.firstOrNull()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (weatherDescription != null) {
            val iconResource = getWeatherIconResource(weatherDescription.icon)
            Image(
                painter = painterResource(id = iconResource),
                contentDescription = weatherDescription.description,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
        }
        Text(text = "Temperature: $temperatureCelsius °C")
        Text(text = "Feels like: $feelsLikeCelsius °C")
        Text(text = "Humidity: ${currentWeather.humidity}%")
        Text(text = "Pressure: ${currentWeather.pressure} hPa")
        Text(text = "Weather: ${weatherDescription?.description}")
    }
}

@Composable
fun HourlyWeatherDisplay(hourlyWeather: List<HourlyWeather>) {
    LazyColumn {
        items(hourlyWeather) { weather ->
            val date = Date(weather.dt * 1000)
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val temperatureCelsius = (weather.temp - 273.15).toInt()
            val weatherDescription = weather.weather.firstOrNull()
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = format.format(date))
                    Text(text = "$temperatureCelsius °C")
                    Text(text = weatherDescription?.description ?: "")
                }
                if (weatherDescription != null) {
                    val iconResource = getWeatherIconResource(weatherDescription.icon)
                    Image(
                        painter = painterResource(id = iconResource),
                        contentDescription = weatherDescription.description,
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}


@Composable
fun DailyWeatherDisplay(dailyWeather: List<DailyWeather>) {
    LazyColumn {
        items(dailyWeather) { weather ->
            val date = Date(weather.dt * 1000)
            val format = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            val dayTempCelsius = (weather.temp.day - 273.15).toInt()
            val nightTempCelsius = (weather.temp.night - 273.15).toInt()
            val minTempCelsius = (weather.temp.min - 273.15).toInt()
            val maxTempCelsius = (weather.temp.max - 273.15).toInt()
            val weatherDescription = weather.weather.firstOrNull()

            Column(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = format.format(date))
                Text(text = "Day: $dayTempCelsius °C")
                Text(text = "Night: $nightTempCelsius °C")
                Text(text = "Min: $minTempCelsius °C, Max: $maxTempCelsius °C")
                Text(text = weatherDescription?.description ?: "")
                if (weatherDescription != null) {
                    val iconUrl = "https://openweathermap.org/img/wn/${weatherDescription.icon}@2x.png"
                    Image(
                        painter = rememberImagePainter(iconUrl),
                        contentDescription = weatherDescription.description,
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
