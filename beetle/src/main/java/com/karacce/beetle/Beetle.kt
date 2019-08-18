package com.karacce.beetle

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Handler
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.karacce.beetle.data.Agent
import com.karacce.beetle.data.Label
import com.karacce.beetle.data.User
import com.karacce.beetle.data.remote.Result
import com.karacce.beetle.ext.sub
import com.karacce.beetle.network.ApiWrapper
import com.karacce.beetle.ui.BeetleActivity
import com.karacce.beetle.ui.snackbar.BeetleSnackbar
import com.karacce.beetle.utils.*
import io.reactivex.disposables.Disposable
import okhttp3.Credentials

/**
 * @user: omerkaraca
 * @date: 2019-07-06
 */

class Beetle(context: Context, private val agent: Agent): ShakeDetector.Listener, CollectDataTask.OnCollectDataTaskListener {

    //region companion

    companion object {

        const val ACTION_SHAKE = "beetle.shake"
        
        private fun with(@NonNull app: Application, @NonNull agent: Agent): Beetle {
            val beetle = Beetle(app.applicationContext, agent)
            app.registerActivityLifecycleCallbacks(Lifecycle(beetle))
            return beetle
        }

        fun azure(@NonNull app: Application, @NonNull namespace: String,
                       @NonNull project: String, @NonNull username: String, @NonNull token: String): Beetle {
            val agent = Agent(Agent.Type.AZURE, Credentials.basic(username, token), namespace, project)
            return with(app, agent)
        }

        fun github(@NonNull app: Application,
                   @NonNull namespace: String,
                   @NonNull project: String,
                   @NonNull token: String): Beetle {
            val agent = Agent(Agent.Type.GITHUB, token, namespace, project)
            return with(app, agent)
        }

        fun gitlab(@NonNull app: Application,
                      @NonNull project: Int,
                      @NonNull token: String): Beetle {
            val agent = Agent(Agent.Type.GITLAB, token, null, project)
            return with(app, agent)
        }
    }

    //endregion

    private var activity: AppCompatActivity? = null
    private var shake: Shake = Shake(this)
    private var collectDataTask: CollectDataTask? = null

    private var snackbar: BeetleSnackbar? = null

    private var subscription: Disposable? = null
    private var api: ApiWrapper = ApiWrapper(agent)
    private var users: List<User> = ArrayList()
    private var labels: List<Label> = ArrayList()

    private var receiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) { return }

            when(intent.action) {
                Constants.ACTION_START -> {
                    snackbar?.dismiss()
                    Handler().postDelayed({
                        activity?.let { activity ->
                            collectDataTask = CollectDataTask(activity, this@Beetle)
                            collectDataTask!!.execute(BitmapUtils.capture(activity.window.decorView.rootView)!!)
                        }
                    }, 250)

                }

                Constants.ACTION_SUBMIT -> {
                    if (intent.hasExtra(Constants.ARG_RESULT)) {
                        submit(Gson().fromJson(intent.getStringExtra(Constants.ARG_RESULT), Result::class.java))
                    }
                }

                ACTION_SHAKE -> {
                    shake.onShake()
                }
            }
        }
    }

    init {
        fetchUsers(); fetchLabels()
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, IntentFilter().apply {
            addAction(Constants.ACTION_START)
            addAction(Constants.ACTION_SUBMIT)
            addAction(ACTION_SHAKE)
        })
    }

    //region network

    private fun fetchUsers() {
        subscription = api.users().sub({ this.users = it })
    }

    private fun fetchLabels() {
        subscription = api.labels()?.sub({ this.labels = it })
    }

    private fun submit(result: Result) {
        api.submit(result) { id, url ->
            if (id != null && url != null) {
                showSuccess(id, url)
            }else { showError() }
        }
    }

    //endregion

    //region ui controller

    override fun onShake() {
        if (activity != null && activity !is BeetleActivity) {
            showConfirmation()
        }
    }

    fun setActivity(activity: AppCompatActivity?) {
        this.activity = activity
        if (activity != null) {
            shake.start(activity)
        }else {
            shake.stop()
        }
    }

    private fun showConfirmation() {
        snackbar?.dismiss()
        if (activity != null) {
            snackbar = BeetleSnackbar.confirmation(activity!!)
            snackbar?.show()
        }
    }

    private fun showSuccess(id: Int, url: String) {
        if (activity != null) {
            //create channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = activity!!.getString(R.string.notification_channel_id)
                val name = activity!!.getString(R.string.notification_channel_name)
                val descriptionText = activity!!.getString(R.string.notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }

                val notificationManager: NotificationManager = activity!!.applicationContext
                    .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            //pending intent
            val pendingIntent: PendingIntent = PendingIntent.getActivity(activity!!, 0,
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(url) }, 0)

            //build notification
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(activity!!, activity!!.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapUtils.drawableToBitmap(ContextCompat.getDrawable(activity!!, when(agent.type) {
                    Agent.Type.AZURE -> R.drawable.ic_azure
                    Agent.Type.GITHUB -> R.drawable.ic_github
                    Agent.Type.GITLAB -> R.drawable.ic_gitlab
                }))).setColor(ContextCompat.getColor(activity!!, R.color.beetle))
                .setContentTitle(activity!!.getString(R.string.success_title, id))
                .setContentText(activity!!.getString(R.string.success_content))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            //notify
            with(NotificationManagerCompat.from(activity!!)) {
                notify(id, builder.build())
            }

        }
    }

    private fun showError() {
        snackbar?.dismiss()
        if (activity != null) {
            snackbar = BeetleSnackbar.error(activity!!)
            snackbar?.show()
        }
    }

    //endregion

    //region CollectDataTask.OnCollectDataTaskListener

    override fun onDataReady(data: Uri?) {
        val shouldStartFeedBack = activity != null && collectDataTask != null
        collectDataTask = null
        if (shouldStartFeedBack && data != null && activity != null) {
            activity!!.startActivity(BeetleActivity.IntentBuilder(activity!!)
                .token(agent.token)
                .users(users)
                .labels(labels)
                .attachment(data)
                .allowAttachment(agent.type != Agent.Type.GITHUB)
                .allowMultipleAssignees(agent.type != Agent.Type.AZURE)
                .build())
        }
    }

    //endregion

}