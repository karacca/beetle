package com.karacca.sample

import android.app.Application
import com.karacca.beetle.Beetle

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Beetle.github(
            this,
            "yourUserName",
            "yourProjectName",
            "yourPersonalAccessToken"
        )
    }
}