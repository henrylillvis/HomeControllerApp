package com.example.androidapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentControlBinding


class ControlFragment : Fragment() {

    data class ButtonMode(var mode: String, var color: Int, var mode_boolean: Boolean) {
    }
    class BtnPress {
        private var clickCount = 0
        var state: ButtonMode? = null // default value off?

        private val btnMode = listOf(
            ButtonMode("OFF", Color.RED, false),
            ButtonMode("ON", Color.GREEN, true),
            ButtonMode("AUTO", Color.YELLOW, true))

        // Selects button mode by click count
        fun selectButtonMode(): ButtonMode? {
            clickCount += 1
            if (clickCount.toString() < btnMode.size.toString()) {
                state =  btnMode[clickCount]
            }
            else {
                clickCount = 0
                state = btnMode[clickCount]
            }
            return state
        }
  }


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentControlBinding>(
            inflater,
            R.layout.fragment_control,container,false)
        val btnPress1 = BtnPress()
        val btnPress2 = BtnPress()
        val button1 = binding.btn1.findViewById<Button>(R.id.btn1)
        val button2 = binding.btn2.findViewById<Button>(R.id.btn2)
        val rele1 = binding.rele1.findViewById<TextView>(R.id.rele1)
        val rele2 = binding.rele2.findViewById<TextView>(R.id.rele2)
        val switch_rele1 = binding.switch1.findViewById<Switch>(R.id.switch1)
        val switch_rele2 = binding.switch2.findViewById<Switch>(R.id.switch2)


        button1.setOnClickListener {
            val buttonMode = btnPress1.selectButtonMode()
            button1?.text = buttonMode?.mode
            buttonMode?.color?.let { it1 -> button1?.setBackgroundColor(it1) }
        }

        button2.setOnClickListener {
            val buttonMode = btnPress2.selectButtonMode()
            button2?.text = buttonMode?.mode
            buttonMode?.color?.let { it2 -> button2?.setBackgroundColor(it2) }
        }

        switch_rele1.setOnClickListener {
            // Switch komponentille löytyy sisäänrakennetut toiminnot jos haluaa niitä käyttää
        }

        switch_rele2.setOnClickListener {

        }


        // TODO

        //Navigointi takaisin home_fragmentille, (Aika turha kylläkin, takaisin napista pääsee myös, voi poistaa)
        binding.controlHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_controlFragment_to_homeFragment)
        }

        return binding.root
    }

}