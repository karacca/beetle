package com.karacca.beetle.data.remote

import com.karacca.beetle.data.Label
import com.karacca.beetle.data.User
import com.karacca.beetle.data.remote.azure.WorkItemComposer
import com.karacca.beetle.data.remote.github.IssueRequest

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

data class Result(
    val title: String,
    val description: String?,
    val assignees: List<User>?,
    val labels: List<Label>?,
    val attachmentPath: String?,
    var attachmentUrl: String? = null
) {

    fun azureWorkItem(): List<WorkItemComposer.Document> {
        val composer = WorkItemComposer.Builder()
        composer.title(title)
        description?.let { composer.description(it) }
        assignees?.let { composer.assignee(it[0]) }
        labels?.let { composer.labels(it) }
        attachmentUrl?.let { composer.attachment(it) }
        return composer.build()
    }

    fun githubIssue() = IssueRequest(title, description,
        assignees?.map { it.descriptor as String }, labels?.map { it.descriptor })

    fun gitlabIssue(): com.karacca.beetle.data.remote.gitlab.IssueRequest {

        return com.karacca.beetle.data.remote.gitlab.IssueRequest(
            title,
            if (attachmentUrl != null) description?.plus("\n\n" + attachmentUrl) else description,
            assignees?.map { (it.descriptor as Double).toInt() },
            labels?.joinToString(separator = ",") { it.descriptor }
        )

    }


}