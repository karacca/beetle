package com.karacca.beetle

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.karacca.beetle.ui.ReportActivity
import com.karacca.beetle.utils.LifecycleHandler
import com.karacca.beetle.utils.Shake
import com.karacca.beetle.utils.ShakeDetector

/**
 * @author karacca
 * @date 12.07.2022
 */

class Beetle private constructor(application: Application) : ShakeDetector.Listener {

    private var activity: AppCompatActivity? = null
    private val shake = Shake(this)

    init {
        application.registerActivityLifecycleCallbacks(LifecycleHandler(this))
    }

    internal fun setActivity(activity: AppCompatActivity?) {
        this.activity = activity
        if (activity != null) {
            shake.start(activity)
        } else {
            shake.stop()
        }
    }

    private fun showConfirmation() {
        activity?.findViewById<View>(android.R.id.content)?.let {
            Snackbar.make(
                it,
                it.context.getString(R.string.confirmation),
                Snackbar.LENGTH_SHORT
            ).setAction(it.context.getString(R.string.confirmation_positive)) {
                val context = activity ?: return@setAction
                context.startActivity(Intent(context, ReportActivity::class.java))
            }.show()
        }
    }

    override fun onShake() {
        showConfirmation()
    }

    companion object {

        @Volatile
        private var INSTANCE: Beetle? = null

        fun getInstance(application: Application): Beetle {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Beetle(application).also { INSTANCE = it }
            }
        }
    }
}
