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

    private var activity: AppCompatActivity? = null
    private val shake = Shake(this)

    private lateinit var gitHubRepository: GitHubRepository

    fun init(
        application: Application,
        organization: String,
        repository: String
    ) {
        application.registerActivityLifecycleCallbacks(LifecycleHandler(this))
        gitHubRepository = GitHubRepository(getPrivateKey(application), organization, repository)
        initialized = true
    }

    override fun onShake() {
        showConfirmation()
    }

    override fun onDataReady(data: Uri?) {
        val context = activity ?: return
        val intent = Intent(context, ReportActivity::class.java)
        intent.putExtra(ReportActivity.ARG_SCREENSHOT, data)
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

    private fun getPrivateKey(context: Context): PrivateKey {
        val file = context.assets.open("beetle.pem")
        val inputStreamReader = InputStreamReader(file)
        val readerBufferedFile = BufferedReader(inputStreamReader)
        val reader = PemReader(readerBufferedFile)
        val privateKeyPem = reader.readPemObject()

        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyPem.content))
    }
}
