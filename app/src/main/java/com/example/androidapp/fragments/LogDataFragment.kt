package com.example.androidapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentLogDataBinding


class LogDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentLogDataBinding>(inflater,
            R.layout.fragment_log_data,container,false)

        // TODO

        //Navigointi takaisin home_fragmentille, (Aika turha kylläkin, takaisin napista pääsee myös, voi poistaa)
        binding.logHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_logDataFragment_to_homeFragment)
        }
        return binding.root
    }

}

