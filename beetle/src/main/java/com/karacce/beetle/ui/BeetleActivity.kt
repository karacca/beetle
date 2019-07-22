package com.karacce.beetle.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.karacce.beetle.Constants
import com.karacce.beetle.R
import com.karacce.beetle.data.Label
import com.karacce.beetle.data.LabelContainer
import com.karacce.beetle.data.User
import com.karacce.beetle.data.UserContainer
import com.karacce.beetle.data.remote.Result
import com.karacce.beetle.ui.adapter.CellAdapter
import kotlinx.android.synthetic.main.activity_beetle.*


/**
 * @user: omerkaraca
 * @date: 2019-07-06
 */

class BeetleActivity : AppCompatActivity() {

    class IntentBuilder(private val context: Context) {
        private val bundle = Bundle()

        fun token(token: String): IntentBuilder {
            bundle.putString(Constants.ARG_TOKEN, token)
            return this
        }

        fun users(users: List<User>): IntentBuilder {
            bundle.putString(Constants.ARG_USERS, Gson().toJson(UserContainer(users)))
            return this
        }

        fun labels(labels: List<Label>): IntentBuilder {
            bundle.putString(Constants.ARG_LABELS, Gson().toJson(LabelContainer(labels)))
            return this
        }

        fun attachment(uri: Uri): IntentBuilder {
            bundle.putParcelable(Constants.ARG_ATTACHMENT, uri)
            return this
        }

        fun allowAttachment(flag: Boolean): IntentBuilder {
            bundle.putBoolean(Constants.ARG_ALLOW_ATTACHMENT, flag)
            return this
        }

        fun allowMultipleAssignees(flag: Boolean): IntentBuilder {
            bundle.putBoolean(Constants.ARG_ALLOW_MULTIPLE_ASSIGNEES, flag)
            return this
        }

        fun build(): Intent = Intent(context, BeetleActivity::class.java).apply { putExtras(bundle) }
    }

    private var users = ArrayList<User>()
    private lateinit var userAdapter: CellAdapter

    private var labels = ArrayList<Label>()
    private lateinit var labelAdapter: CellAdapter

    private var attachment: Uri? = null
    private var includeAttachment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beetle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (intent.hasExtra(Constants.ARG_USERS)) {
            users.addAll(Gson().fromJson(intent.getStringExtra(Constants.ARG_USERS), UserContainer::class.java).items)
            if (users.isNotEmpty()) {
                setUserAdapter(
                    intent.getBooleanExtra(Constants.ARG_ALLOW_MULTIPLE_ASSIGNEES, false),
                    intent.getStringExtra(Constants.ARG_TOKEN)
                )
            }
        }

        if (intent.hasExtra(Constants.ARG_LABELS)) {
            labels.addAll(Gson().fromJson(intent.getStringExtra(Constants.ARG_LABELS), LabelContainer::class.java).items)
            if (labels.isNotEmpty()) {
                setLabelAdapter()
            }
        }

        if (intent.hasExtra(Constants.ARG_ATTACHMENT) &&
            intent.getBooleanExtra(Constants.ARG_ALLOW_ATTACHMENT, false)) {
            attachment = intent.getParcelableExtra(Constants.ARG_ATTACHMENT)
            includeAttachment = true

            attachmentButton.visibility = View.VISIBLE
            attachmentButton.setOnClickListener {
                includeAttachment = includeAttachment.not()
                attachmentButton.setImageResource(if (includeAttachment) R.drawable.ic_attachment_selected else R.drawable.ic_attachment)
            }
        }

        back.setOnClickListener { finish() }
        send.setOnClickListener { submit() }

        issueTitle.requestFocus()
    }

    private fun showError(@StringRes message: Int) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun submit() {

        if (issueTitle.text.isNullOrEmpty()) {
            showError(R.string.beetle_issue_title_warning)
            return
        }

        if (appVersion.text.isNullOrEmpty()) {
            showError(R.string.beetle_issue_app_version_warning)
            return
        }

        var description = issueDescription.text?.toString()
        description += getDeviceInfo()
        val result = Result(issueTitle.text.toString(),
            description,
            if (users.any { it.selected }) users.filter { it.selected } else null,
            if (labels.any { it.selected }) labels.filter { it.selected } else null,
            if (includeAttachment) attachment?.path else null)

        LocalBroadcastManager.getInstance(this)
            .sendBroadcast(Intent(Constants.ACTION_SUBMIT).apply {
                val bundle = Bundle()
                bundle.putString(Constants.ARG_RESULT, Gson().toJson(result))
                putExtras(bundle)
            }).also { finish() }
    }

    private fun setUserAdapter(allowMultipleAssignee: Boolean, token: String?) {
        userAdapter = CellAdapter(users, token) { p ->
            if (!allowMultipleAssignee) {
                for (i: Int in 0 until users.size) {
                    if (i != p) {
                        users[i].selected = false
                    } else {
                        users[i].selected = users[i].selected.not()
                    }
                }
            } else {
                users[p].selected = users[p].selected.not()
            }
            userAdapter.notifyDataSetChanged()
        }
        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        userRecyclerView.adapter = userAdapter
        userLayout.visibility = View.VISIBLE
        userDivider.visibility = View.VISIBLE
    }

    private fun setLabelAdapter() {
        labelAdapter = CellAdapter(labels) { p ->
            labels[p].selected = labels[p].selected.not()
            labelAdapter.notifyDataSetChanged()
        }
        labelRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        labelRecyclerView.adapter = labelAdapter
        labelLayout.visibility = View.VISIBLE
        labelDivider.visibility = View.VISIBLE
    }

    private fun getDeviceInfo(): String {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val dpi = resources.displayMetrics.density * 160f
        return "\n\n\n### Device Information\n" +
                "- Model : ${Build.MODEL}\n" +
                "- Manufacture : ${Build.MANUFACTURER}\n" +
                "- SDK Version : ${Build.VERSION.SDK_INT}\n" +
                "- Client Version : ${appVersion.text.toString()}\n" +
                "- Resolution : $width x $height\n" +
                "- Density : $dpi dpi"
    }

}