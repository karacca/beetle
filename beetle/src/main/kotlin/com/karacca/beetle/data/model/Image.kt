package com.karacca.beetle.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author karacca
 * @date 28.07.2022
 */

internal data class Image(
    @SerializedName("image")
    val image: Image?,
) {

    data class Image(
        @SerializedName("url")
        val url: String?
    )
}
