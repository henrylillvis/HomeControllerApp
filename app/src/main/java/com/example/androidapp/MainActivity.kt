package com.example.androidapp



import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.example.androidapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listener: OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Loads Shared preferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Setup a shared preference listener for hpwAddress and restart transport
        /*
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(prefs, String key) {
                if (key.equals(/*key for shared pref you're listening for*/) {
                        //Do stuff; restart activity in your case
                    } }

            prefs.registerOnSharedPreferenceChangeListener(listener);
        */
    }

}