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

@file:Suppress("MemberVisibilityCanBePrivate")

package com.karacca.beetle

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.View
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

@SuppressLint("StaticFieldLeak", "MemberVisibilityCanBePrivate")
object Beetle : ShakeDetector.Listener, CollectDataTask.OnCollectDataTaskListener {

    private var config = BeetleConfig()
    private var activity: Activity? = null
    private val shake = Shake(this)

    fun init(
        application: Application,
        organization: String,
        repository: String
    ) {
        application.registerActivityLifecycleCallbacks(LifecycleHandler(this))
        configure {
            this.organization = organization
            this.repository = repository
        }
    }

    fun configure(block: BeetleConfig.() -> Unit) = config.apply(block)

    @Suppress("DEPRECATION")
    fun startFeedback() {
        if (config.initialized && activity != null) {
            Handler().postDelayed({
                val task = CollectDataTask(activity!!, this)
                BitmapUtils.capture(activity!!.window) {
                    if (it != null) {
                        task.execute(it)
                    } else {
                        showError()
                    }
                }
            }, 100)
        }
    }

    internal fun setActivity(activity: Activity?) {
        this.activity = activity
        if (activity != null && config.initialized) {
            shake.start(activity)
        } else {
            shake.stop()
        }
    }

    internal fun showError() {
        activity?.findViewById<View>(android.R.id.content)?.let {
            Snackbar.make(
                it,
                it.context.getString(R.string.common_error),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun showFeedbackConfirmation() {
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

    override fun onShake() {
        showFeedbackConfirmation()
    }

    override fun onDataReady(data: Uri?) {
        if (config.initialized && activity != null) {
            val intent = Intent(activity!!, FeedbackActivity::class.java)
            intent.putExtra(FeedbackActivity.ARG_SCREENSHOT, data)
            intent.putExtra(FeedbackActivity.ARG_CONFIG, Gson().toJson(config))
            activity!!.startActivity(intent)
        }
    }
}
