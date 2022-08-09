package com.karacca.beetle

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.karacca.beetle.ui.ReportActivity
import com.karacca.beetle.utils.*

/**
 * @author karacca
 * @date 12.07.2022
 */

object Beetle : ShakeDetector.Listener, CollectDataTask.OnCollectDataTaskListener {

    private var initialized = false

    private lateinit var organization: String
    private lateinit var repository: String

    private var activity: AppCompatActivity? = null
    private val shake = Shake(this)

    private val customData = bundleOf()

    fun init(
        application: Application,
        organization: String,
        repository: String
    ) {
        application.registerActivityLifecycleCallbacks(LifecycleHandler(this))
        this.organization = organization
        this.repository = repository
        initialized = true
    }

    fun setCustomKey(key: String, value: String) {
        customData.putString(key, value)
    }

    fun setCustomKey(key: String, value: Boolean) {
        customData.putBoolean(key, value)
    }

    fun setCustomKey(key: String, value: Double) {
        customData.putDouble(key, value)
    }

    fun setCustomKey(key: String, value: Float) {
        customData.putFloat(key, value)
    }

    fun setCustomKey(key: String, value: Int) {
        customData.putInt(key, value)
    }

    override fun onShake() {
        showConfirmation()
    }

    override fun onDataReady(data: Uri?) {
        val context = activity ?: return
        val intent = Intent(context, ReportActivity::class.java)
        intent.putExtra(ReportActivity.ARG_SCREENSHOT, data)
        intent.putExtra(ReportActivity.ARG_CUSTOM_DATA, customData)
        intent.putExtra(ReportActivity.ARG_ORGANIZATION, organization)
        intent.putExtra(ReportActivity.ARG_REPOSITORY, repository)
        context.startActivity(intent)
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
            val snackBar = Snackbar.make(
                it,
                it.context.getString(R.string.send_feedback_confirmation),
                Snackbar.LENGTH_SHORT
            )

            snackBar.view.setOnClickListener {
                snackBar.dismiss()
                startFeedback()
            }

            snackBar.show()
        }
    }

    @Suppress("DEPRECATION")
    private fun startFeedback() {
        Handler().postDelayed({
            val task = CollectDataTask(activity!!, this)
            task.execute(BitmapUtils.capture(activity!!.window.decorView.rootView))
        }, 100)
    }
}
