package com.example.androidapp.screens.home


import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.example.androidapp.HomeController
import com.example.androidapp.R
import com.example.androidapp.databinding.FragmentHomeBinding
import com.example.androidapp.network.HomeApi
import com.example.androidapp.network.StatesProperty
import com.example.androidapp.network.WeatherProperty
import kotlinx.coroutines.*
import java.io.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var home = ""
    private var weather: WeatherProperty? = null
    private var states: StatesProperty? = null
    private val apiVM: HomeApi by activityViewModels()
    private var searching = false
    private val _response = MutableLiveData<String>()
    private var Sipuli: Bitmap ? = null

    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private var image_uri: Uri? = null
        private val IMAGE_CAPTURE_CODE = 654

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.refreshButton.setOnClickListener {
            if (!searching) {
                hideData()
                getData()
            }
        }
        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                 ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val permission = arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 112)
                openCamera()
            } else {
                ActivityCompat.requestPermissions(
                    requireContext() as Activity,
                    arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
        if (savedInstanceState != null){
            Sipuli = savedInstanceState.getParcelable("uri");
            binding.homeImage.setImageBitmap(Sipuli)
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

    private fun openCamera() {
        val resolver: ContentResolver = requireContext().getContentResolver()
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME,"HOME")
        values.put(MediaStore.Images.Media.TITLE, "Koti kuva")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Kakkaa tuulettimeen")
        image_uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
                Sipuli = uriToBitmap(image_uri!!)!!
                binding.homeImage.setImageBitmap(Sipuli)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        if (Sipuli != null) {
            outState.putParcelable("uri", Sipuli)
        }
    }
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val resolver: ContentResolver = requireContext().getContentResolver()
            val parcelFileDescriptor = resolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
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