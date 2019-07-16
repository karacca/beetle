package com.karacce.beetle.data.remote.gitlab

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

data class IssueResponse(
    @SerializedName("iid") val id: Int,
    @SerializedName("web_url") val url: String
)