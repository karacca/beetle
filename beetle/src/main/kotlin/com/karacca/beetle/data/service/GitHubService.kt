/*
 * Copyright 2022 Omer Karaca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karacca.beetle.data.service

import com.google.gson.Gson
import com.karacca.beetle.BuildConfig
import com.karacca.beetle.data.model.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * @author karacca
 * @date 19.07.2022
 */

internal interface GitHubService {

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

    companion object {
        private const val BASE_URL = "https://api.github.com"
        private val ACCEPT_HEADER = Pair("Accept", "application/vnd.github.v3+json")

        fun newInstance(): GitHubService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            }

            val headerInterceptor = Interceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.addHeader(ACCEPT_HEADER.first, ACCEPT_HEADER.second)
                chain.proceed(builder.build())
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(GitHubService::class.java)
        }
    }
}
