package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author karacca
 * @date 25.07.2022
 */

internal data class Label(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("name")
    val name: String,
    @SerializedName("color")
    val color: String? = null
) {
    var selected = false
}
