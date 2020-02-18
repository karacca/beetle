package com.karacca.beetle.ui.snackbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.karacca.beetle.R
import com.karacca.beetle.data.Agent
import com.karacca.beetle.ext.findSuitableParent

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

class BeetleSnackbar(
    parent: ViewGroup,
    content: BeetleSnackbarView
) : BaseTransientBottomBar<BeetleSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun confirmation(activity: AppCompatActivity): BeetleSnackbar {
            val view = activity.window.decorView.findViewById(android.R.id.content) as View
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.")
            val customView = LayoutInflater.from(view.context).inflate(R.layout.layout_snackbar, parent, false) as BeetleSnackbarView
            customView.confirmation()
            return BeetleSnackbar(parent, customView)
        }

        fun success(activity: AppCompatActivity, agent: Agent.Type, id: Int, url: String): BeetleSnackbar {
            val view = activity.window.decorView.findViewById(android.R.id.content) as View
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.")
            val customView = LayoutInflater.from(view.context).inflate(R.layout.layout_snackbar, parent, false) as BeetleSnackbarView
            customView.success(agent, id, url)
            return BeetleSnackbar(parent, customView)
        }

        fun error(activity: AppCompatActivity): BeetleSnackbar {
            val view = activity.window.decorView.findViewById(android.R.id.content) as View
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.")
            val customView = LayoutInflater.from(view.context).inflate(R.layout.layout_snackbar, parent, false) as BeetleSnackbarView
            customView.error()
            return BeetleSnackbar(parent, customView)
        }

    }

}
