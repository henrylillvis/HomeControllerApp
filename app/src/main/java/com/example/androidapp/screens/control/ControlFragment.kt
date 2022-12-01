package com.example.androidapp.screens.control

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentControlBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.androidapp.network.HomeApi
import com.example.androidapp.network.StatesProperty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class ControlFragment : Fragment() {
    data class ButtonMode(var mode: String, var color: Int, var mode_boolean: Boolean)
    class BtnPress {
        var button: Button? = null
        var state: ButtonMode? = null
        private var clickCount = 0

        private fun modifyButton(){
            button?.text = state?.mode
            state?.color?.let { it -> button?.setBackgroundColor(it) }
        }

        fun setState (s: String) {
            val result = btnMode.filter{ s == it.mode }[0]
            clickCount = btnMode.indexOf(result)
            state = btnMode[clickCount]
            modifyButton()
        }
        // Three button modes and specs
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
            modifyButton()
            return state
        }
  }
    private var states: StatesProperty? = null
    private val apiVM: HomeApi by activityViewModels()
    private val btnPress1 = BtnPress()
    private val btnPress2 = BtnPress()

    var s1pointer: Switch? = null
    var s2pointer: Switch? = null

    // Function gets states from database and shows correct relay state after opening controlfragment
    private fun getStates () {
            lifecycleScope.launch{
                try {
                    states = apiVM.service?.getStates()
                    btnPress1.setState(states?.relayOne.toString())
                    btnPress2.setState(states?.relayTwo.toString())
                    if (states?.relayThree.toString() == "ON") s1pointer?.isChecked = true
                    if (states?.relayFour.toString() == "ON") s2pointer?.isChecked = true
                }
                catch (e: Exception){
                    Toast.makeText(context,"${e.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
    override fun onResume() {
        super.onResume()
        getStates()
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

        getStates()
        val button1 = binding.btn1.findViewById<Button>(R.id.btn1)
        val button2 = binding.btn2.findViewById<Button>(R.id.btn2)
        val switch1 = binding.switch1.findViewById<Switch>(R.id.switch1)
        val switch2 = binding.switch2.findViewById<Switch>(R.id.switch2)

        btnPress1.button = button1
        btnPress2.button = button2
        s1pointer = switch1
        s2pointer = switch2

        button1.setOnClickListener {
            val buttonMode = btnPress1.selectButtonMode()
            lifecycleScope.launch{
                apiVM.service?.setState(1, buttonMode?.mode.toString())
            }
        }
        button2.setOnClickListener {
            val buttonMode = btnPress2.selectButtonMode()
            lifecycleScope.launch{
                apiVM.service?.setState(2, buttonMode?.mode.toString())
            }
        }
        switch1.setOnClickListener {
                lifecycleScope.launch{
                    apiVM.service?.setState(3, (if (switch1.isChecked) "ON" else "OFF"))
            }
        }
        switch2.setOnClickListener {
            lifecycleScope.launch{
                apiVM.service?.setState(4, (if (switch2.isChecked) "ON" else "OFF"))
            }
        }

        // TODO

        //Navigointi takaisin home_fragmentille, (Aika turha kylläkin, takaisin napista pääsee myös, voi poistaa)
        binding.controlHomeButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_controlFragment_to_homeFragment)
        }

        return binding.root
    }

}