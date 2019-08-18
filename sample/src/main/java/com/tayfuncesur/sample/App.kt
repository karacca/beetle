package com.tayfuncesur.sample

import android.app.Application
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.karacce.beetle.Beetle
import com.karacce.beetle.utils.ShakeDetector

class App : Application() {

    lateinit var beetle: Beetle

    override fun onCreate() {
        super.onCreate()
        beetle = Beetle.github(this, "yourUserName", "yourProjectName", "yourPersonalAccessToken")
    }
}