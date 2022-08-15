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

package com.karacca.beetle.utils

import android.content.Context
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
        buildConfigFields: HashMap<String, Any>,
        deviceData: HashMap<String, Any>,
        customData: HashMap<String, Any>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("## ${context.getString(R.string.description)}")
        sb.appendLine(description)

        if (buildConfigFields.isNotEmpty()) {
            sb.appendLine("## ${context.getString(R.string.build_config)}")
            val property = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$property|$value|")
            sb.appendLine("|:-|:-|")

            for (key in buildConfigFields.keys) {
                sb.appendLine("|$key|${buildConfigFields[key]}|")
            }
        }

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
