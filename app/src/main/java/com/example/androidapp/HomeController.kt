package com.example.androidapp

import android.app.Application
import android.content.Context

// get application context using this class
class HomeController : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context? = null
        val appContext: Context?
            get() = context
    }
}