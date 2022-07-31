package com.karacca.beetle.network

import com.karacca.beetle.network.model.*
import retrofit2.http.*

/**
 * @author karacca
 * @date 19.07.2022
 */

interface GitHubService {

    @GET("repos/{org}/{repo}/installation")
    suspend fun getRepositoryInstallation(
        @Header("Authorization") authHeader: String,
        @Path("org") org: String,
        @Path("repo") repo: String
    ): RepositoryInstallation

    @POST("app/installations/{installationId}/access_tokens")
    suspend fun createAccessToken(
        @Header("Authorization") authHeader: String,
        @Path("installationId") installationId: Int,
    ): AccessToken

    @GET("repos/{org}/{repo}/collaborators")
    suspend fun getCollaborators(
        @Header("Authorization") authHeader: String,
        @Path("org") org: String,
        @Path("repo") repo: String
    ): List<Collaborator>

    @GET("repos/{org}/{repo}/labels")
    suspend fun getLabels(
        @Header("Authorization") authHeader: String,
        @Path("org") org: String,
        @Path("repo") repo: String
    ): List<Label>

    @POST("repos/{org}/{repo}/issues")
    suspend fun createIssue(
        @Header("Authorization") authHeader: String,
        @Path("org") org: String,
        @Path("repo") repo: String,
        @Body issueRequest: IssueRequest
    ): Issue
}
