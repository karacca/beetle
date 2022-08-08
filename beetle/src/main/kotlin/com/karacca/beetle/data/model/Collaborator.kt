package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author karacca
 * @date 22.07.2022
 */

internal data class Collaborator(
    @SerializedName("login")
    val login: String,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("avatar_url")
    val avatarUrl: String? = null
) {

    var selected = false
}
