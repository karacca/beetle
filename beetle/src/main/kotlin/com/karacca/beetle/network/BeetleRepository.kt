package com.karacca.beetle.network

import android.net.Uri
import androidx.core.net.toFile
import com.google.gson.Gson
import com.karacca.beetle.BuildConfig
import com.karacca.beetle.network.model.*
import io.jsonwebtoken.Jwts
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
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

    private var gitHubToken: AccessToken? = null
    private val gitHubService: GitHubService = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .addInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder.addHeader("Accept", "application/vnd.github.v3+json")
                    chain.proceed(builder.build())
                }.build()
        ).baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build().create(GitHubService::class.java)

    private val imageService = Retrofit.Builder()
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
                .build()
        ).baseUrl("https://freeimage.host/api/1/")
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build().create(ImageService::class.java)

    suspend fun getCollaborators(): List<Collaborator> {
        return this.gitHubService.getCollaborators(getAccessTokenHeader(), org, repo)
    }

    suspend fun getLabels(): List<Label> {
        return this.gitHubService.getLabels(getAccessTokenHeader(), org, repo)
    }

    suspend fun createIssue(
        title: String,
        description: String,
        assignees: List<String>,
        labels: List<String>,
        screenshot: Uri
    ): Issue {
        val image = uploadImage(screenshot)

        return this.gitHubService.createIssue(
            getAccessTokenHeader(),
            org,
            repo,
            IssueRequest(title, "$description ${image.image!!.url}", assignees, labels)
        )
    }

    private suspend fun uploadImage(screenshot: Uri): Image {
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
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
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
}
