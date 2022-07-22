package com.karacca.sample

import android.app.Application
import com.karacca.beetle.Beetle

/**
 * @author karacca
 * @date 12.07.2022
 */

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        Beetle.init(this, "karacca", "Beetle")
    }
}
