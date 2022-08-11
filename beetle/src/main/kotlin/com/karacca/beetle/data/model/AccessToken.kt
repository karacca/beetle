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

package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * @author karacca
 * @date 22.07.2022
 */

internal data class AccessToken(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("expires_at")
    val expiresAt: String? = null
) {

    fun isValid(): Boolean {
        if (token == null || expiresAt == null) {
            return false
        }

        val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.parse(expiresAt)!! > Date()
    }

    companion object {
        @Suppress("SpellCheckingInspection")
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
