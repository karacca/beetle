package com.karacca.beetle.network.model

/**
 * @author karacca
 * @date 28.07.2022
 */

data class IssueRequest(
    val title: String,
    val body: String,
    val assignees: List<String>,
    val labels: List<String>
)
