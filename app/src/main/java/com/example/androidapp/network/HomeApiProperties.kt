package com.example.androidapp.network
import com.squareup.moshi.Json

// return value for setting a state (OK, NOT OK, NO RESPONSE)
data class StateProperty(
    @Json(name = "STATE") val state: String
)

// TEMP2 and HUMID are from same sensor, all float values
data class WeatherProperty(
    @Json(name = "TEMP1") val temp1: Double,
    @Json(name = "TEMP2") val temp2: Double,
    @Json(name = "HUMID") val humid: Double
)

// states are STATE or WEATHER and values consist n amount of float values
data class LogProperty(
    @Json(name = "timestamp") val timestamp: String,
    @Json(name = "action") val action: String,
    @Json(name = "values") val values: List<Double>
)

// possible relay states (ON, OFF, AUTO)
data class StatesProperty(
    @Json(name = "1") val relayOne: String,
    @Json(name = "2") val relayTwo: String,
    @Json(name = "3") val relayThree: String,
    @Json(name = "4") val relayFour: String,
)