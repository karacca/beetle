package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author karacca
 * @date 28.07.2022
 */

internal data class Issue(
    @SerializedName("number")
    val number: Int? = null,
    @SerializedName("html_url")
    val htmlUrl: String?
)
