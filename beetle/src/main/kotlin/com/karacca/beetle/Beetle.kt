package com.karacca.beetle

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.karacca.beetle.ui.FeedbackActivity
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
    private var config = BeetleConfig()

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

    fun configure(block: BeetleConfig.() -> Unit) {
        config.apply(block)
    }

    override fun onShake() {
        showConfirmation()
    }

    override fun onDataReady(data: Uri?) {
        val context = activity ?: return
        val intent = Intent(context, FeedbackActivity::class.java)
        intent.putExtra(FeedbackActivity.ARG_SCREENSHOT, data)
        intent.putExtra(FeedbackActivity.ARG_CUSTOM_DATA, customData)
        intent.putExtra(FeedbackActivity.ARG_ORGANIZATION, organization)
        intent.putExtra(FeedbackActivity.ARG_REPOSITORY, repository)
        intent.putExtra(FeedbackActivity.ARG_CONFIG, Gson().toJson(config))
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
