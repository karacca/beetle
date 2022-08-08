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
