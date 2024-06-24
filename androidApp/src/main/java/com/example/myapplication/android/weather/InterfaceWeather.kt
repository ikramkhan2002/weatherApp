package com.example.myapplication.android.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "Enter Your API Key"
const val BASE_URL = "https://api.openweathermap.org/data/3.0/"

data class WeatherResponse(
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
)

data class CurrentWeather(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val weather: List<WeatherDescription>
)

data class HourlyWeather(
    val dt: Long,
    val temp: Double,
    val weather: List<WeatherDescription>
)

data class DailyWeather(
    val dt: Long,
    val temp: DailyTemp,
    val weather: List<WeatherDescription>
)

data class DailyTemp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

interface WeatherApi {
    @GET("onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") appid: String = API_KEY
    ): WeatherResponse
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: WeatherApi by lazy {
        retrofit.create(WeatherApi::class.java)
    }
}
