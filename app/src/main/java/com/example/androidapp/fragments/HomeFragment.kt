package com.example.androidapp.fragments

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)

        // TODO

        // Navigointi
        binding.settingsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        binding.controlButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_controlFragment)
        }
        binding.logButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_logDataFragment)
        }
        return binding.root
    }



}