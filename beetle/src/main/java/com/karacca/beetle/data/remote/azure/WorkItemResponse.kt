package com.karacca.beetle.data.remote.azure

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-14
 */

data class WorkItemResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String
)