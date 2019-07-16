package com.karacce.beetle.data.remote.github

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

data class IssueResponse(
    val number: Int,
    @SerializedName("html_url") val url: String
)