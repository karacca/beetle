package com.karacca.beetle

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.karacca.beetle.network.GitHubRepository
import com.karacca.beetle.ui.ReportActivity
import com.karacca.beetle.utils.*
import org.bouncycastle.util.io.pem.PemReader
import java.io.BufferedReader
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec

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

    override fun onShake() {
        showConfirmation()
    }

    override fun onDataReady(data: Uri?) {
        val context = activity ?: return
        val intent = Intent(context, ReportActivity::class.java)
        intent.putExtra(ReportActivity.ARG_SCREENSHOT, data)
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
                it.context.getString(R.string.confirmation),
                Snackbar.LENGTH_SHORT
            )

            snackBar.setAction(it.context.getString(R.string.confirmation_positive)) {
                snackBar.dismiss()
                Handler().postDelayed({
                    val task = CollectDataTask(activity!!, this)
                    task.execute(BitmapUtils.capture(activity!!.window.decorView.rootView))
                }, 250)
            }

            snackBar.show()
        }
    }
}
