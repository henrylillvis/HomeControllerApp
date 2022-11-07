package com.example.androidapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentControlBinding
import com.example.androidapp.databinding.FragmentHomeBinding


class ControlFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentControlBinding>(inflater,
            R.layout.fragment_control,container,false)

        // TODO


        //Navigointi takaisin home_fragmentille, (Aika turha kylläkin, takaisin napista pääsee myös, voi poistaa)
        binding.controlHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_controlFragment_to_homeFragment)
        }

        return binding.root
    }

}