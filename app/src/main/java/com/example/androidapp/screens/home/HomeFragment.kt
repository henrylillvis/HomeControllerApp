package com.example.androidapp.screens.home


import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var home = ""
    private var weather: WeatherProperty? = null
    private var states: StatesProperty? = null
    private val apiVM: HomeApi by activityViewModels()
    private var searching = false
    private val _response = MutableLiveData<String>()

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        //binding.lifecycleOwner = this

        binding.refreshButton.setOnClickListener {
            if (!searching) {
                hideData()
                getData()
            }
        }
        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //camera intent
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                ActivityCompat.requestPermissions(
                    requireContext() as Activity,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == CAMERA_REQUEST_CODE) {
                val photo = data!!.extras!!["data"] as Bitmap?
                binding.homeImage.setImageBitmap(photo)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun getData() {
        d("jb", "getData started")
        lifecycleScope.launch {
            searching = true
            binding.progressbar.visibility = View.VISIBLE
            try {

                //Service ei kerkeä latautumaan ensimmäisellä kerralla?
                if (apiVM.service == null) {
                    d("jb", apiVM.service.toString())
                    delay(1000)
                    if (apiVM.service == null) {
                        throw Exception("Virhe yhteyden muodostamisessa.")
                    }
                }

                weather = apiVM.service?.getWeather()
                states = apiVM.service?.getStates()
                _response.value = "Haku onnistui"
                setTemperatures()
                setRelayStates()
                showData()
            } catch (e: Exception) {
                val errorMsg = "Haku epäonnistui: ${e.message}"
                _response.value = errorMsg
                showError(errorMsg)
            }
            binding.progressbar.visibility = View.GONE
            d("jb", "Api response: " + _response.value)
            searching = false
        }
    }

    private fun showData() {
        binding.inTempView.visibility = View.VISIBLE
        binding.outTempView.visibility = View.VISIBLE
        binding.relayView.visibility = View.VISIBLE
    }

    private fun hideData() {
        binding.errorCard.visibility = View.GONE
        binding.inTempView.visibility = View.GONE
        binding.outTempView.visibility = View.GONE
        binding.relayView.visibility = View.GONE
    }

    private fun showError(msg: String?) {
        binding.errorMessage.text = msg
        binding.errorCard.visibility = View.VISIBLE
    }

    private fun setTemperatures() {
        binding.inTemp.text = weather?.temp2.toString() + " °C"
        binding.outTemp.text = weather?.temp1.toString() + " °C"
        binding.inHum.text = weather?.humid.toString() + " %"
    }

    private fun setRelayStates() {
        binding.relay1State.text = states?.relayOne
        binding.relay2State.text = states?.relayTwo
        binding.relay3State.text = states?.relayThree
        binding.relay4State.text = states?.relayFour
    }

    private fun setHome() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
        home = sharedPreferences.getString("home_signature", "").toString()
        binding.homeSign.text = home
    }

}