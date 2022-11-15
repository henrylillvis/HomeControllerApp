package com.example.androidapp.fragments

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.androidapp.R
import androidx.fragment.app.activityViewModels
import com.example.androidapp.network.HomeApi



/**
 *  xml-tiedosto löytyy res -> xml -> root_preferences
 */


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?){
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        val nameSignature = findPreference<EditTextPreference>("signature")
        val homeSignature = findPreference<EditTextPreference>("home_signature")
        val serverSignature = findPreference<EditTextPreference>("server_signature")
        val apiVM: HomeApi by activityViewModels()

        // force ipv4 address and set correct address to apiViewModel
        serverSignature?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            if (newValue.toString().matches("""^((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)(\.(?!$)|$)){4}$""".toRegex())) {
                apiVM._address.value = newValue.toString()
                true
            } else {
                false
            }
        }
    }

}



