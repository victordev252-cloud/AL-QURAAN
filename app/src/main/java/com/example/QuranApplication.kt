package com.example

import android.app.Application
import android.util.Log

class QuranApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("QuranApplication", "Quran Karim Application initialized.")
    }
}
