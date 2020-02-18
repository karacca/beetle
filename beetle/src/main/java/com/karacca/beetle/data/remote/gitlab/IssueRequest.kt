package com.karacca.beetle.data.remote.gitlab

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

data class IssueRequest(
    val title: String,
    val description: String?,
    @SerializedName("assignee_ids") val assignees: List<Int>?,
    val labels: String?
)