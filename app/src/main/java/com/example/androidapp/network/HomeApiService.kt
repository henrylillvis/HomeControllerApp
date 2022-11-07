package com.example.androidapp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

// https://developer.android.com/codelabs/kotlin-android-training-internet-data?index=..%2F..android-kotlin-fundamentals#3

// Tähän asetetaan ip-osoite, joka annetaan asetuksissa
private val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"; //Testi

/**
 * Voisi implementoida jonkun JSON-parserin, Moshia käytetty AKF:ssä
 */

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface HomeApiService {
    /**
     * Jere muokkaa rajapinnan
     */
    @GET("temperatures")
    fun getTemperatures(): Call<String>

    @GET("logdata")
    fun getLogData(): Call<String>

}

object HomeApi {
    val retrofitService : HomeApiService by lazy {
        retrofit.create(HomeApiService::class.java) }
}
