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
        appAndDeviceData: List<Pair<String, Any>>,
        userAttributes: List<Pair<String, Any>>
    ): String {
        val sb = StringBuilder()
        sb.appendLine("## ${context.getString(R.string.description)}")
        sb.appendLine(description)

        if (appAndDeviceData.isNotEmpty()) {
            sb.appendLine("## ${context.getString(R.string.app_and_device_data)}")
            val property = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$property|$value|")
            sb.appendLine("|:-|:-|")

            for ((k, v) in appAndDeviceData) {
                sb.appendLine("|**$k**|$v|")
            }
        }

        if (userAttributes.isNotEmpty()) {
            sb.appendLine("## ${context.getString(R.string.user_attributes)}")
            val key = context.getString(R.string.key)
            val value = context.getString(R.string.value)
            sb.appendLine("|$key|$value|")
            sb.appendLine("|:-|:-|")

            for ((k, v) in userAttributes) {
                sb.appendLine("|**$k**|$v|")
            }
        }

        if (imageUrl != null) {
            sb.appendLine("## ${context.getString(R.string.feedback_screenshot_title)}")
            sb.appendLine("<img src=\"$imageUrl\" alt=\"screenshot\" width=\"200\"/>")
        }

        return sb.toString()
    }
}
