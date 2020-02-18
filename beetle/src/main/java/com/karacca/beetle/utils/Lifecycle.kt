package com.karacca.beetle.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karacca.beetle.Beetle

/**
 * @user: omerkaraca
 * @date: 2019-07-06
 */

class Lifecycle(private var beetle: Beetle): Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(activity: Activity?) {
        beetle.setActivity(null)
    }

    override fun onActivityResumed(activity: Activity?) {
        with(activity as AppCompatActivity) {
            beetle.setActivity(activity)
        }
    }

    override fun onActivityStarted(activity: Activity?) { }

    override fun onActivityDestroyed(activity: Activity?) { }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) { }

    override fun onActivityStopped(activity: Activity?) { }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) { }
}