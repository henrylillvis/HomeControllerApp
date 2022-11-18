package com.example.androidapp.screens.home


import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.androidapp.HomeController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentHomeBinding
import com.example.androidapp.network.StatesProperty
import com.example.androidapp.network.WeatherProperty



class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    var user = ""
    var home = ""
    lateinit var weather: WeatherProperty
    lateinit var states: StatesProperty

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)

        d("jb","Näkymä ajetaan")
        /**
         * Navigointi
         */
        binding.settingsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        binding.controlButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_controlFragment)
        }
        binding.logButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_logDataFragment)
        }
        updateTemperatures()
        updateStates()
        setUser()
        getHome()
        setHome()
        setTemperatures()
        setRelayStates()
        return binding.root
    }
    private fun setHome() {
        binding.homeSign.text = home

    }
    private fun setTemperatures() {
        binding.inTemp.text = weather.temp2.toString() + " °C"
        binding.outTemp.text = weather.temp1.toString() + " °C"
        binding.inHum.text = weather.humid.toString() + " %"
    }
    private fun setRelayStates(){
        binding.relay1State.text = states.relayOne
        binding.relay2State.text = states.relayTwo
        binding.relay3State.text = states.relayThree
        binding.relay4State.text = states.relayFour
    }

    private fun updateTemperatures() {
        weather = WeatherProperty(-5.2,23.2,12.5)
    }

    private fun updateStates() {
        states = StatesProperty("AUTO","ON","OFF","ON")
    }

    private fun setUser() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        user = sharedPreferences.getString("signature", "").toString()
    }

    private fun getHome() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        home = sharedPreferences.getString("home_signature", "").toString()
        d("jb", home)
    }

}