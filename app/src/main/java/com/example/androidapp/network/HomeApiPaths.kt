package com.example.androidapp.network

import retrofit2.http.GET
import retrofit2.http.Path

//    run from activity like this:

//    val apiVM: HomeApi by activityViewModels()
//    lifecycleScope.launch {
//        apiVM.service?.getWeather()
//    }

interface HomeApiPaths {
    @GET("/state/{relay}/{state}")
    suspend fun setState(@Path(value="relay") relay: Int, @Path(value="state") state: String): StateProperty

    @GET("/weather")
    suspend fun getWeather(): WeatherProperty

    @GET("/states")
    suspend fun getStates(): StatesProperty

    @GET("/logs")
    suspend fun getLogs(): List<LogProperty>

    // @GET("/light") ?
}

