package com.karacca.beetle.data.repository

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.net.toFile
import com.karacca.beetle.BuildConfig
import com.karacca.beetle.data.model.*
import com.karacca.beetle.data.service.GitHubService
import com.karacca.beetle.data.service.ImageService
import io.jsonwebtoken.Jwts
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.security.PrivateKey
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author karacca
 * @date 20.07.2022
 */

internal class BeetleRepository(
    private val privateKey: PrivateKey,
    private val org: String,
    private val repo: String
) {

    private val gitHubService = GitHubService.newInstance()
    private val imageService = ImageService.newInstance()
    private var gitHubToken: AccessToken? = null

    suspend fun getCollaborators(): List<Collaborator> {
        return this.gitHubService.getCollaborators(getAccessTokenHeader(), org, repo)
    }

    suspend fun getLabels(): List<Label> {
        return this.gitHubService.getLabels(getAccessTokenHeader(), org, repo)
    }

    suspend fun createIssue(
        title: String,
        descriptionMarkdown: String,
        assignees: List<String>,
        labels: List<String>
    ): Issue {
        return this.gitHubService.createIssue(
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

    suspend fun uploadImage(screenshot: Uri): Image {
        val file = screenshot.toFile()
        return imageService.uploadImage(
            BuildConfig.FREE_IMAGE_KEY.toRequestBody(),
            MultipartBody.Part.createFormData(
                "source",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        )
    }

    private suspend fun getRepositoryInstallation(): Int {
        return this.gitHubService.getRepositoryInstallation(getJWTokenHeader(), org, repo).id!!
    }

    private suspend fun createAccessToken(installationId: Int): AccessToken {
        return this.gitHubService.createAccessToken(getJWTokenHeader(), installationId).also {
            this.gitHubToken = it
        }
    }

    private suspend fun getAccessTokenHeader(): String {
        val token = if (
            gitHubToken?.token != null &&
            SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(gitHubToken!!.expiresAt!!)!! > Date()
        ) {
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

    private fun createDescriptionMarkdown(
        description: String,
        imageUrl: String,
        customData: Bundle
    ): String {
        return """
            ## Description
            $description

            ## Device Info
            |Property|Value|
            |:-|:-|
            |Brand|${Build.BRAND}|
            |Model|${Build.MODEL}|
            |Android Version|${Build.VERSION.SDK_INT}|
            
            ## Custom Data
            |Property|Value|
            |:-|:-|

            ## Screenshot
            <img src="$imageUrl" alt="screenshot" width="200"/> 
        """.trimIndent()
    }

    @Suppress("SpellCheckingInspection")
    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    }
}
