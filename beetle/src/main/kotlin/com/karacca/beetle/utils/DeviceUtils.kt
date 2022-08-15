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
import android.content.Context.WINDOW_SERVICE
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import com.karacca.beetle.R
import java.util.*

/**
 * @author karacca
 * @date 7.08.2022
 */

internal object DeviceUtils {

    private const val KEY_APP_ID = "APPLICATION_ID"
    private const val KEY_VERSION_NAME = "VERSION_NAME"
    private const val KEY_VERSION_CODE = "VERSION_CODE"
    private const val KEY_BUILD_TYPE = "BUILD_TYPE"

    fun getApplicationData(context: Context): List<Pair<String, Any>> {
        val data = arrayListOf<Pair<String, Any>>()
        getAppId(context)?.let { data.add(context.getString(R.string.app_id) to it) }
        getAppVersion(context)?.let { data.add(context.getString(R.string.app_version) to it) }
        getBuildType(context)?.let { data.add(context.getString(R.string.app_build_type) to it) }
        return data
    }

    fun getDeviceData(context: Context): List<Pair<String, Any>> {
        return arrayListOf(
            context.getString(R.string.device) to getBrandAndModel(),
            context.getString(R.string.os_version) to getVersion(),
            context.getString(R.string.language) to getLanguage(),
            context.getString(R.string.display) to getDisplaySizeAndOrientation(context)
        )
    }

    private fun getBrandAndModel() = "${Build.BRAND} ${Build.MODEL}"

    private fun getVersion() = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"

    private fun getLanguage() = Locale.getDefault().displayLanguage

    private fun getDisplaySizeAndOrientation(context: Context): String {
        val sb = StringBuilder()

        try {
            val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                sb.append(windowManager.maximumWindowMetrics.bounds.width())
                sb.append("x")
                sb.append(windowManager.maximumWindowMetrics.bounds.height())
            } else {
                val size = Point()
                windowManager.defaultDisplay.getRealSize(size)
                sb.append(size.x)
                sb.append("x")
                sb.append(size.y)
            }

            sb.append(" ")
        } catch (e: Exception) {
        }

        when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> sb.append(
                "(${context.getString(R.string.portrait)})"
            )

            Configuration.ORIENTATION_LANDSCAPE -> sb.append(
                "(${context.getString(R.string.landscape)})"
            )

            else -> {
            }
        }

        return sb.toString()
    }

    private fun getAppId(context: Context) = getBuildConfigValue(context, KEY_APP_ID)

    private fun getAppVersion(context: Context): Any? {
        val versionName = getBuildConfigValue(context, KEY_VERSION_NAME)
        val versionCode = getBuildConfigValue(context, KEY_VERSION_CODE)
        return if (versionName != null && versionCode != null) {
            "$versionName ($versionCode)"
        } else {
            null
        }
    }

    private fun getBuildType(context: Context) = getBuildConfigValue(context, KEY_BUILD_TYPE)

    private fun getBuildConfigValue(context: Context, fieldName: String): Any? {
        return try {
            val clazz = Class.forName(context.packageName + ".BuildConfig")
            val field = clazz.getField(fieldName)
            field.get(null)
        } catch (e: Exception) {
            null
        }
    }
}
