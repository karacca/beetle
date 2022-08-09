package com.karacca.beetle.utils

import android.content.Context
import android.os.Bundle
import com.karacca.beetle.R

/**
 * @author karacca
 * @date 8.08.2022
 */

internal object MarkdownUtils {

    fun createDescription(
        context: Context,
        description: String,
        imageUrl: String?,
        deviceData: HashMap<String, Any>,
        customData: HashMap<String, Any>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("## ${context.getString(R.string.description)}")
        sb.appendLine(description)

        if (deviceData.isNotEmpty()) {
            sb.appendLine("## ${context.getString(R.string.device_info)}")
            val property = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$property|$value|")
            sb.appendLine("|:-|:-|")

            for (key in deviceData.keys) {
                sb.appendLine("|$key|${deviceData[key]}|")
            }
        }

        if (customData.isNotEmpty()) {
            sb.appendLine("## ${context.getString(R.string.custom_data)}")
            val key = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$key|$value|")
            sb.appendLine("|:-|:-|")

            for (k in customData.keys) {
                sb.appendLine("|$k|${customData[k]}|")
            }
        }

        if (imageUrl != null) {
            sb.appendLine("## ${context.getString(R.string.feedback_screenshot_title)}")
            sb.appendLine("<img src=\"$imageUrl\" alt=\"screenshot\" width=\"200\"/>")
        }

        return sb.toString()
    }
}
