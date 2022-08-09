/*
 * Copyright 2022 Omer Karaca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.karacca.beetle.utils.BitmapUtils
import com.karacca.beetle.utils.CollectDataTask
import com.karacca.beetle.utils.LifecycleHandler
import com.karacca.beetle.utils.Shake
import com.karacca.beetle.utils.ShakeDetector

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

    fun configure(config: BeetleConfig) {
        this.config = config
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
