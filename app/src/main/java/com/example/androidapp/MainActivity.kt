package com.example.androidapp



import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.example.androidapp.databinding.ActivityMainBinding
import com.example.androidapp.network.HomeApi
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var listener: OnSharedPreferenceChangeListener? = null
    private lateinit var apiVM: HomeApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        apiVM = ViewModelProvider(this)[HomeApi::class.java]

        //Loads Shared preferences
        val prefs = PreferenceManager.getDefaultSharedPreferences(this);
        handlePrefs(prefs)

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

    // handle sharedpreferences on application start
    fun handlePrefs(sharedPreferences: SharedPreferences){
        val prefEditor = sharedPreferences.edit()
        lifecycleScope.launch {
            val t = apiVM.service?.getMotion()!!.motion
            // set value to preference manually, as this doesn't really work with coroutines
            prefEditor.putString("pir_signature", t.toString())
            prefEditor.commit()
        }

        val darkTheme = sharedPreferences.getBoolean("theme", false)
        if (darkTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

}

