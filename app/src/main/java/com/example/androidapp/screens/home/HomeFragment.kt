package com.example.androidapp.screens.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

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
        /**
         * Set data
         */
        setHome()
        setTemperatures()
        setRelayStates()
        return binding.root
    }

    private fun setHome() {
        binding.homeSign.text = viewModel.home
    }
    private fun setTemperatures() {
        binding.inTemp.text = viewModel.weather.temp2.toString()
        binding.outTemp.text = viewModel.weather.temp1.toString()
        binding.inHum.text = viewModel.weather.humid.toString()
    }
    private fun setRelayStates(){
        binding.relay1State.text = viewModel.states.relayOne
        binding.relay2State.text = viewModel.states.relayTwo
        binding.relay3State.text = viewModel.states.relayThree
        binding.relay4State.text = viewModel.states.relayFour
    }

}