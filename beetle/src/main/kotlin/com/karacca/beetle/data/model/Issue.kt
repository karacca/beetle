package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author karacca
 * @date 28.07.2022
 */

internal data class Issue(
    @SerializedName("url")
    val url: String?
)
