package com.karacca.beetle.network.model
import com.google.gson.annotations.SerializedName


/**
 * @author karacca
 * @date 28.07.2022
 */
 
data class Issue(
    @SerializedName("active_lock_reason")
    val activeLockReason: Any?,
    @SerializedName("assignee")
    val assignee: Assignee?,
    @SerializedName("assignees")
    val assignees: List<Assignee?>?,
    @SerializedName("author_association")
    val authorAssociation: String?,
    @SerializedName("body")
    val body: String?,
    @SerializedName("closed_at")
    val closedAt: Any?,
    @SerializedName("closed_by")
    val closedBy: Any?,
    @SerializedName("comments")
    val comments: Int?,
    @SerializedName("comments_url")
    val commentsUrl: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("events_url")
    val eventsUrl: String?,
    @SerializedName("html_url")
    val htmlUrl: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("labels")
    val labels: List<Label?>?,
    @SerializedName("labels_url")
    val labelsUrl: String?,
    @SerializedName("locked")
    val locked: Boolean?,
    @SerializedName("milestone")
    val milestone: Any?,
    @SerializedName("node_id")
    val nodeId: String?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("performed_via_github_app")
    val performedViaGithubApp: Any?,
    @SerializedName("reactions")
    val reactions: Reactions?,
    @SerializedName("repository_url")
    val repositoryUrl: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("state_reason")
    val stateReason: Any?,
    @SerializedName("timeline_url")
    val timelineUrl: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("user")
    val user: User?
) {
    data class Assignee(
        @SerializedName("avatar_url")
        val avatarUrl: String?,
        @SerializedName("events_url")
        val eventsUrl: String?,
        @SerializedName("followers_url")
        val followersUrl: String?,
        @SerializedName("following_url")
        val followingUrl: String?,
        @SerializedName("gists_url")
        val gistsUrl: String?,
        @SerializedName("gravatar_id")
        val gravatarId: String?,
        @SerializedName("html_url")
        val htmlUrl: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("login")
        val login: String?,
        @SerializedName("node_id")
        val nodeId: String?,
        @SerializedName("organizations_url")
        val organizationsUrl: String?,
        @SerializedName("received_events_url")
        val receivedEventsUrl: String?,
        @SerializedName("repos_url")
        val reposUrl: String?,
        @SerializedName("site_admin")
        val siteAdmin: Boolean?,
        @SerializedName("starred_url")
        val starredUrl: String?,
        @SerializedName("subscriptions_url")
        val subscriptionsUrl: String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Label(
        @SerializedName("color")
        val color: String?,
        @SerializedName("default")
        val default: Boolean?,
        @SerializedName("description")
        val description: String?,
        @SerializedName("id")
        val id: Long?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("node_id")
        val nodeId: String?,
        @SerializedName("url")
        val url: String?
    )

    data class Reactions(
        @SerializedName("confused")
        val confused: Int?,
        @SerializedName("eyes")
        val eyes: Int?,
        @SerializedName("heart")
        val heart: Int?,
        @SerializedName("hooray")
        val hooray: Int?,
        @SerializedName("laugh")
        val laugh: Int?,
        @SerializedName("rocket")
        val rocket: Int?,
        @SerializedName("total_count")
        val totalCount: Int?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("+1")
        val plus1: Int?,
        @SerializedName("-1")
        val minus1: Int?
    )

    data class User(
        @SerializedName("avatar_url")
        val avatarUrl: String?,
        @SerializedName("events_url")
        val eventsUrl: String?,
        @SerializedName("followers_url")
        val followersUrl: String?,
        @SerializedName("following_url")
        val followingUrl: String?,
        @SerializedName("gists_url")
        val gistsUrl: String?,
        @SerializedName("gravatar_id")
        val gravatarId: String?,
        @SerializedName("html_url")
        val htmlUrl: String?,
        @SerializedName("id")
        val id: Int?,
        @SerializedName("login")
        val login: String?,
        @SerializedName("node_id")
        val nodeId: String?,
        @SerializedName("organizations_url")
        val organizationsUrl: String?,
        @SerializedName("received_events_url")
        val receivedEventsUrl: String?,
        @SerializedName("repos_url")
        val reposUrl: String?,
        @SerializedName("site_admin")
        val siteAdmin: Boolean?,
        @SerializedName("starred_url")
        val starredUrl: String?,
        @SerializedName("subscriptions_url")
        val subscriptionsUrl: String?,
        @SerializedName("type")
        val type: String?,
        @SerializedName("url")
        val url: String?
    )
}
