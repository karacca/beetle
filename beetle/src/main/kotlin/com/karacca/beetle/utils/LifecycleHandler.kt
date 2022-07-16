package com.karacca.beetle.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karacca.beetle.Beetle

/**
 * @author karacca
 * @date 12.07.2022
 */

internal class LifecycleHandler(private val beetle: Beetle) :
    Application.ActivityLifecycleCallbacks {

    override fun onActivityResumed(p0: Activity) {
        beetle.setActivity(p0 as? AppCompatActivity)
    }

    override fun onActivityPaused(p0: Activity) {
        beetle.setActivity(null)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    override fun onActivityStarted(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}

}
