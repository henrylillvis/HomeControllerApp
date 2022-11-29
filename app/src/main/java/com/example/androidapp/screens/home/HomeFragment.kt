package com.example.androidapp.screens.home


import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.androidapp.HomeController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentHomeBinding
import com.example.androidapp.network.StatesProperty
import com.example.androidapp.network.WeatherProperty
import androidx.lifecycle.*
import com.example.androidapp.network.HomeApi
import kotlinx.coroutines.*
import java.lang.Exception


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var home = ""
    private var weather: WeatherProperty? = null
    private var states: StatesProperty? = null
    private val apiVM: HomeApi by activityViewModels()
    private var searching = false

    private val _response = MutableLiveData<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        //binding.lifecycleOwner = this

        binding.refreshButton.setOnClickListener{
            if(!searching){
                hideData()
                getData()
            }
        }
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
        setHome()
        getData()
        return binding.root
    }

    private fun getData() {
        d("jb","getData started")
        lifecycleScope.launch {
            searching = true
            binding.progressbar.visibility = View.VISIBLE
            try {

                //Service ei kerkeä latautumaan ensimmäisellä kerralla?
                if(apiVM.service == null){
                    d("jb",apiVM.service.toString())
                    delay(1000)
                    if (apiVM.service == null){throw Exception("Virhe yhteyden muodostamisessa.")}
                }

                weather = apiVM.service?.getWeather()
                states = apiVM.service?.getStates()
                _response.value = "Haku onnistui"
                setTemperatures()
                setRelayStates()
                showData()
            }catch (e: Exception){
                val errorMsg = "Haku epäonnistui: ${e.message}"
                _response.value = errorMsg
                showError(errorMsg)
            }
            binding.progressbar.visibility = View.GONE
            d("jb","Api response: "+_response.value )
            searching = false
        }
    }
    private fun showData(){
        binding.inTempView.visibility = View.VISIBLE
        binding.outTempView.visibility = View.VISIBLE
        binding.relayView.visibility = View.VISIBLE
    }
    private fun hideData(){
        binding.errorCard.visibility = View.GONE
        binding.inTempView.visibility = View.GONE
        binding.outTempView.visibility = View.GONE
        binding.relayView.visibility = View.GONE
    }

    private fun showError(msg: String?){
        binding.errorMessage.text = msg
        binding.errorCard.visibility = View.VISIBLE
    }

    private fun setTemperatures() {
        binding.inTemp.text = weather?.temp2.toString() + " °C"
        binding.outTemp.text = weather?.temp1.toString() + " °C"
        binding.inHum.text = weather?.humid.toString() + " %"
    }

    private fun setRelayStates(){
        binding.relay1State.text = states?.relayOne
        binding.relay2State.text = states?.relayTwo
        binding.relay3State.text = states?.relayThree
        binding.relay4State.text = states?.relayFour
    }

    private fun setHome() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        home = sharedPreferences.getString("home_signature", "").toString()
        binding.homeSign.text = home
    }
}