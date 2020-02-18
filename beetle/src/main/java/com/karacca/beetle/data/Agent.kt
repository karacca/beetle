package com.karacca.beetle.data

import android.content.Context
import androidx.core.content.ContextCompat
import com.karacca.beetle.R

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

data class Agent(
    val type: Type,
    val token: String,
    val namespace: String?,
    val project: Any) {

    enum class Type(val value: Int) {
        AZURE(0), GITHUB(1), GITLAB(2);

        fun description(context: Context): String = context.getString(when(this) {
            AZURE -> R.string.azure_description
            GITHUB -> R.string.github_description
            GITLAB -> R.string.gitlab_description
        })

        fun color(context: Context): Int = when(this) {
            AZURE -> ContextCompat.getColor(context, R.color.azure)
            GITHUB -> ContextCompat.getColor(context, R.color.github)
            GITLAB -> ContextCompat.getColor(context, R.color.gitlab)
        }

        companion object {
            fun find(agent: Int) = values().first { it.value == agent }
        }
    }
}