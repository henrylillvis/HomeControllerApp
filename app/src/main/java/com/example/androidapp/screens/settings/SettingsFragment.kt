package com.example.androidapp.screens.settings

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatDelegate
import com.example.androidapp.R
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import com.example.androidapp.HomeController
import com.example.androidapp.network.HomeApi
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/**
 *  xml-tiedosto löytyy res -> xml -> root_preferences
 */


class SettingsFragment : PreferenceFragmentCompat() {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeController.appContext)
    private val apiVM: HomeApi by activityViewModels()

    override fun onResume() {
        super.onResume()

        val prefEditor = sharedPreferences.edit()
        lifecycleScope.launch {
            try{
            val t = apiVM.service?.getMotion()!!.motion
            // set value to preference manually, as this doesn't really work with coroutines
            prefEditor.putString("pir_signature", t.toString())
            prefEditor.commit()
        }
            catch (e:Exception){
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?){
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val nameSignature = findPreference<EditTextPreference>("signature")
        val homeSignature = findPreference<EditTextPreference>("home_signature")
        val serverSignature = findPreference<EditTextPreference>("server_signature")
        val pirSignature = findPreference<EditTextPreference>("pir_signature")
        val theme = findPreference<SwitchPreferenceCompat>("theme")

        val prefEditor = sharedPreferences.edit()

        // force ipv4 address + port and set to apiViewModel
        serverSignature?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (newValue.toString().matches("""^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]):[0-9]+${'$'}""".toRegex())) {
                apiVM._address.value = newValue.toString()
                true
            } else {
                false
            }
        }

        pirSignature?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (newValue.toString().toIntOrNull() != null) {
                val seconds = newValue.toString()
                lifecycleScope.launch {
                    val t = apiVM.service?.setMotion(seconds.toInt())!!.motion
                    // set value to preference manually, as this doesn't really work with coroutines
                    if(t == seconds.toInt()){
                        prefEditor.putString("pir_signature", seconds)
                        prefEditor.commit()
                    }
                }
            }
            false
        }

        theme?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (newValue == true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            true
        }

    }

}



