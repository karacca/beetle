package com.karacca.beetle.network

import com.google.gson.Gson
import com.karacca.beetle.BuildConfig
import com.karacca.beetle.network.model.AccessToken
import com.karacca.beetle.network.model.Collaborator
import com.karacca.beetle.network.model.Label
import io.jsonwebtoken.Jwts
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.PrivateKey
import java.text.SimpleDateFormat
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

    private var accessToken: AccessToken? = null
    private val service: GitHubService = Retrofit.Builder()
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

    suspend fun getCollaborators(): List<Collaborator> {
        return service.getCollaborators(getAccessTokenHeader(), org, repo)
    }

    suspend fun getLabels(): List<Label> {
        return service.getLabels(getAccessTokenHeader(), org, repo)
    }

    private suspend fun getRepositoryInstallation(): Int {
        return service.getRepositoryInstallation(getJWTokenHeader(), org, repo).id!!
    }

    private suspend fun createAccessToken(installationId: Int): AccessToken {
        return service.createAccessToken(getJWTokenHeader(), installationId).also {
            this.accessToken = it
        }
    }

    private suspend fun getAccessTokenHeader(): String {
        val token = if (
            accessToken?.token != null &&
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.parse(accessToken!!.expiresAt!!)!! > Date()
        ) {
            accessToken!!.token!!
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
