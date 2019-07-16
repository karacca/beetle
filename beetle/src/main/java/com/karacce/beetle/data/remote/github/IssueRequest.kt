package com.karacce.beetle.data.remote.github

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

data class IssueRequest(
    private val title: String,
    private val body: String?,
    private val assignees: List<String>?,
    private val labels: List<String>?
)