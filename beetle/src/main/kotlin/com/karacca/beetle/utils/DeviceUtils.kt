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
import android.os.Build
import com.karacca.beetle.R

/**
 * @author karacca
 * @date 7.08.2022
 */

internal object DeviceUtils {

    fun getDeviceData(context: Context): HashMap<String, Any> {
        return hashMapOf(
            context.getString(R.string.brand) to Build.BRAND,
            context.getString(R.string.model) to Build.MODEL,
            context.getString(R.string.version_key) to context.getString(
                R.string.version_value,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT
            )
        )
    }
}
