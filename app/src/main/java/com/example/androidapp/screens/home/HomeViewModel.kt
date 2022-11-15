package com.example.androidapp.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.androidapp.HomeController
import com.example.androidapp.network.StatesProperty
import com.example.androidapp.network.WeatherProperty

class HomeViewModel: ViewModel() {

    var user = ""
    var home = ""
    lateinit var weather: WeatherProperty
    lateinit var states: StatesProperty
    /**
     * Get temperature data from API
     */
    private fun updateTemperatures() {
        weather = WeatherProperty(-5.2,23.2,12.5)
    }
    /**
     * Get current states from API
     */
    private fun updateStates() {
       states = StatesProperty("AUTO","ON","OFF","ON")
    }
    /**
     * Set app user
     */
    private fun setUser() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        user = sharedPreferences.getString("signature", "").toString()
    }
    /**
     * Set home address/name
     */
    private fun setHome() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        home = sharedPreferences.getString("home_signature", "").toString()
    }
    init {
        setUser()
        setHome()
        updateTemperatures()
        updateStates()
        Log.i("HomeViewModel", "HomeViewModel created!")
    }
    /**
     * Callback called when the ViewModel is destroyed
     */
    override fun onCleared() {
        super.onCleared()
        Log.i("HomeViewModel", "HomeViewModel destroyed!")
    }
}