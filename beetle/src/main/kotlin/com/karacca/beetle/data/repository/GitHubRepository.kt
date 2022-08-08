package com.karacca.beetle.data.repository

import com.karacca.beetle.BuildConfig
import com.karacca.beetle.data.model.*
import com.karacca.beetle.data.service.GitHubService
import io.jsonwebtoken.Jwts
import java.security.PrivateKey
import java.util.*

/**
 * @author karacca
 * @date 20.07.2022
 */

internal class GitHubRepository(
    private val privateKey: PrivateKey,
    private val org: String,
    private val repo: String
) {

    private val gitHubService = GitHubService.newInstance()
    private var gitHubToken: AccessToken? = null

    suspend fun getCollaborators(): List<Collaborator> {
        return gitHubService.getCollaborators(getAccessTokenHeader(), org, repo)
    }

    suspend fun getLabels(): List<Label> {
        return gitHubService.getLabels(getAccessTokenHeader(), org, repo)
    }

    suspend fun createIssue(
        title: String,
        descriptionMarkdown: String,
        assignees: List<String>,
        labels: List<String>
    ): Issue {
        return gitHubService.createIssue(
            getAccessTokenHeader(),
            org,
            repo,
            IssueRequest(
                title,
                descriptionMarkdown,
                assignees,
                labels
            )
        )
    }

    private suspend fun getRepositoryInstallation(): Int {
        return gitHubService.getRepositoryInstallation(getJWTokenHeader(), org, repo).id!!
    }

    private suspend fun createAccessToken(installationId: Int): AccessToken {
        return gitHubService.createAccessToken(getJWTokenHeader(), installationId).also {
            this.gitHubToken = it
        }
    }

    private suspend fun getAccessTokenHeader(): String {
        val token = if (gitHubToken?.isValid() == true) {
            gitHubToken!!.token!!
        } else {
            createAccessToken(getRepositoryInstallation()).token!!
        }

        return "token $token"
    }

    private fun getJWTokenHeader(): String {
        val current = Date().time
        val token = Jwts.builder()
            .setIssuer(BuildConfig.GITHUB_APP_ID)
            .setIssuedAt(Date(current - 60))
            .setExpiration(Date(current + (10 * 60)))
            .signWith(privateKey)
            .compact()

        return "Bearer $token"
    }
}
