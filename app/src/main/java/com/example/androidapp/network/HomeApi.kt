package com.example.androidapp.network

import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.example.androidapp.HomeController
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory



// custom viewmodel class for creating a retrofit service and updating it everytime address is changed (in settingsfragment)
class HomeApi: ViewModel() {
    public var _address = MutableLiveData<String>("127.0.0.1")
    val address: LiveData<String>
        get() = _address
    private var _service = MutableLiveData<HomeApiPaths>()
    private val service: LiveData<HomeApiPaths>
        get() = _service

    init {
        // get first time value from preference
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        val addr = sharedPreferences.getString("server_signature", "")
        _address.value = addr.toString()

        // listen for changes to address and create new service immediately
        viewModelScope.launch {
            address.asFlow().collect {
                createService()
            }
        }
    }

    // create service with newly set address
    private fun createService(){
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        _service = MutableLiveData<HomeApiPaths>(
                    Retrofit.Builder()
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .baseUrl("http://${address.value.toString()}")
                        .build().create(HomeApiPaths::class.java))
    }


    // wrapper functions to run API calls without explicit coroutine in anywhere else

    fun getStates(): StatesProperty{
        var res = StatesProperty("UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN")
        viewModelScope.launch {
            try {
                res = service.value?.getStates() ?: throw Exception()
            } catch (e: Exception) { }
        }
        return res
    }

    fun getLogs(): List<LogProperty>{
        var res = emptyList<LogProperty>()
        viewModelScope.launch {
            try {
                res = service.value?.getLogs() ?: throw Exception()
            } catch (e: Exception) { }
        }
        return res
    }

    fun getWeather(): WeatherProperty{
        var res = WeatherProperty(0.0, 0.0, 0.0)
        viewModelScope.launch {
            try {
                res = service.value?.getWeather() ?: throw Exception()
            } catch (e: Exception) { }
        }
        return res
    }

    fun setState(relay: Int, state: String): StateProperty{
        var res = StateProperty("UNKNOWN")
        viewModelScope.launch {
            try {
                res = service.value?.setState(relay, state) ?: throw Exception()
            } catch (e: Exception) { }
        }
        return res
    }
}
