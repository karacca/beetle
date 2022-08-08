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
        deviceData: Bundle?,
        customData: Bundle?
    ): String {
        val sb = StringBuilder()
        sb.appendLine("## ${context.getString(R.string.description)}")
        sb.appendLine(description)

        if (deviceData != null) {
            sb.appendLine("## ${context.getString(R.string.device_info)}")
            val property = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$property|$value|")
            sb.appendLine("|:-|:-|")

            for (key in deviceData.keySet()) {
                sb.appendLine("|$key|${deviceData[key]}|")
            }
        }

        if (customData != null) {
            sb.appendLine("## ${context.getString(R.string.custom_data)}")
            val key = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$key|$value|")
            sb.appendLine("|:-|:-|")

            for (k in customData.keySet()) {
                sb.appendLine("|$k|${customData[k]}|")
            }
        }

        if (imageUrl != null) {
            sb.appendLine("## ${context.getString(R.string.screenshot)}")
            sb.appendLine("<img src=\"$imageUrl\" alt=\"screenshot\" width=\"200\"/>")
        }

        return sb.toString()
    }
}
