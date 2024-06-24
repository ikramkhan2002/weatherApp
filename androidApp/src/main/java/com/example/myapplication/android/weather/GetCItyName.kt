package com.example.myapplication.android.weather

import android.content.Context
import android.location.Geocoder
import java.util.Locale

fun getCityName(context: Context, lat: Double, lon: Double): String? {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        addresses!![0]?.locality
    } catch (e: Exception) {
        null
    }
}
