package com.example.androidapp.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.androidapp.R

/**
 *  xml-tiedosto löytyy res -> xml -> root_preferences
 */

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}