package com.example.androidapp.screens.settings

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatDelegate
import com.example.androidapp.R
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.*
import com.example.androidapp.network.HomeApi
import kotlinx.coroutines.launch


/**
 *  xml-tiedosto löytyy res -> xml -> root_preferences
 */


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?){
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val nameSignature = findPreference<EditTextPreference>("signature")
        val homeSignature = findPreference<EditTextPreference>("home_signature")
        val serverSignature = findPreference<EditTextPreference>("server_signature")
        val pirSignature = findPreference<EditTextPreference>("pir_signature")
        val theme = findPreference<SwitchPreferenceCompat>("theme")
        val apiVM: HomeApi by activityViewModels()

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
            if (newValue.toString().matches("""^[]?\d+${'$'}""".toRegex())) {
                val seconds = newValue.toString().toInt()
                lifecycleScope.launch {
                    if (apiVM.service?.setPir(seconds)!!.seconds == seconds){
                        true
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



