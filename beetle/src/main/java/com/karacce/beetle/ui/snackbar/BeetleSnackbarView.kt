package com.karacce.beetle.ui.snackbar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.snackbar.ContentViewCallback
import com.karacce.beetle.Beetle
import com.karacce.beetle.Constants
import com.karacce.beetle.R
import com.karacce.beetle.data.Agent
import com.karacce.beetle.ext.dp
import com.karacce.beetle.ext.px
import kotlinx.android.synthetic.main.view_snackbar.view.*

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

class BeetleSnackbarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    init {
        View.inflate(context, R.layout.view_snackbar, this)
        clipToPadding = false
    }

    fun confirmation() {
        title.text = context.getString(R.string.confirmation_title)
        content.text = context.getString(R.string.confirmation_content)
        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_beetle))

        setOnClickListener {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(Constants.ACTION_START))
        }
    }

    fun success(agent: Agent.Type, id: Int, url: String) {
        title.text = context.getString(R.string.success_title, id)
        content.text = context.getString(R.string.success_content)

        icon.setPadding(8.px, 8.px, 8.px, 8.px)
        icon.setImageDrawable(
            ContextCompat.getDrawable(
                context, when (agent) {
                    Agent.Type.AZURE -> R.drawable.ic_azure
                    Agent.Type.GITHUB -> R.drawable.ic_github
                    Agent.Type.GITLAB -> R.drawable.ic_gitlab
                }
            )
        )

        setOnClickListener {
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(Constants.ACTION_SHOW)
                .apply { putExtras(Bundle().apply { putString(Constants.ARG_URL, url) }) })
        }
    }

    fun error() {
        title.text = context.getString(R.string.error_title)
        content.text = context.getString(R.string.error_content)
        icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_beetle))
    }

    override fun animateContentIn(delay: Int, duration: Int) {}

    override fun animateContentOut(delay: Int, duration: Int) {}
}