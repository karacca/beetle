package com.karacca.beetle.network.model

import com.google.gson.annotations.SerializedName

/**
 * @author karacca
 * @date 20.07.2022
 */

data class RepositoryInstallation(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("account")
    val account: Account? = null,
    @SerializedName("repository_selection")
    val repositorySelection: String? = null,
    @SerializedName("access_tokens_url")
    val accessTokensUrl: String? = null,
    @SerializedName("repositories_url")
    val repositoriesUrl: String? = null,
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @SerializedName("app_id")
    val appId: Int? = null,
    @SerializedName("app_slug")
    val appSlug: String? = null,
    @SerializedName("target_id")
    val targetId: Int? = null,
    @SerializedName("target_type")
    val targetType: String? = null,
    @SerializedName("permissions")
    val permissions: Permissions? = null,
    @SerializedName("events")
    val events: List<Any?>? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("single_file_name")
    val singleFileName: Any? = null,
    @SerializedName("has_multiple_single_files")
    val hasMultipleSingleFiles: Boolean? = null,
    @SerializedName("single_file_paths")
    val singleFilePaths: List<Any?>? = null,
    @SerializedName("suspended_by")
    val suspendedBy: Any? = null,
    @SerializedName("suspended_at")
    val suspendedAt: Any? = null
) {
    data class Account(
        @SerializedName("login")
        val login: String? = null,
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("node_id")
        val nodeId: String? = null,
        @SerializedName("avatar_url")
        val avatarUrl: String? = null,
        @SerializedName("gravatar_id")
        val gravatarId: String? = null,
        @SerializedName("url")
        val url: String? = null,
        @SerializedName("html_url")
        val htmlUrl: String? = null,
        @SerializedName("followers_url")
        val followersUrl: String? = null,
        @SerializedName("following_url")
        val followingUrl: String? = null,
        @SerializedName("gists_url")
        val gistsUrl: String? = null,
        @SerializedName("starred_url")
        val starredUrl: String? = null,
        @SerializedName("subscriptions_url")
        val subscriptionsUrl: String? = null,
        @SerializedName("organizations_url")
        val organizationsUrl: String? = null,
        @SerializedName("repos_url")
        val reposUrl: String? = null,
        @SerializedName("events_url")
        val eventsUrl: String? = null,
        @SerializedName("received_events_url")
        val receivedEventsUrl: String? = null,
        @SerializedName("type")
        val type: String? = null,
        @SerializedName("site_admin")
        val siteAdmin: Boolean? = null
    )

    data class Permissions(
        @SerializedName("issues")
        val issues: String? = null,
        @SerializedName("metadata")
        val metadata: String? = null
    )
}
