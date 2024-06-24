package com.example.myapplication.android.weather

import com.example.myapplication.android.R

fun getWeatherIconResource(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.icon_default_day // Clear sky (day)
        "01n" -> R.drawable.icon_default_night // Clear sky (night)
        "02d" -> R.drawable.icon_few_clouds_day // Few clouds (day)
        "02n" -> R.drawable.icon_few_clouds_night // Few clouds (night)
        "03d", "03n" -> R.drawable.icon_scattered_clouds // Scattered clouds
        "04d", "04n" -> R.drawable.icon_broken_clouds // Broken clouds
        "09d", "09n" -> R.drawable.icon_shower_rain // Shower rain
        "10d" -> R.drawable.icon_rain_day // Rain (day)
        "10n" -> R.drawable.icon_rain_night // Rain (night)
        "11d", "11n" -> R.drawable.icon_thunderstorm // Thunderstorm
        "13d", "13n" -> R.drawable.icon_snow // Snow
        "50d", "50n" -> R.drawable.icon_mist // Mist
        else -> R.drawable.icon_default // Default icon for any other conditions
    }
}
