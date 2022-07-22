package com.karacca.beetle.network

import com.karacca.beetle.network.model.RepositoryInstallation
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

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

}
