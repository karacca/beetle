package com.karacca.beetle.data.remote.azure

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-14
 */

data class Attachment(
    @SerializedName("id") val id: String,
    @SerializedName("url") val url: String
)