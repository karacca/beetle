package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName


/**
 * @author karacca
 * @date 22.07.2022
 */

data class AccessToken(
    @SerializedName("token")
    val token: String? = null,
    @SerializedName("expires_at")
    val expiresAt: String? = null,
    @SerializedName("permissions")
    val permissions: Permissions? = null,
    @SerializedName("repository_selection")
    val repositorySelection: String? = null
) {
    data class Permissions(
        @SerializedName("issues")
        val issues: String? = null,
        @SerializedName("metadata")
        val metadata: String? = null
    )
}
