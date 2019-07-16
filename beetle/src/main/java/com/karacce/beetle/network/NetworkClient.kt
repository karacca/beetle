package com.karacce.beetle.network

import com.karacce.beetle.network.service.AzureService
import com.karacce.beetle.network.service.GithubService
import com.karacce.beetle.network.service.GitlabService
import com.karacce.beetle.network.service.VisualStudioService
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */


object NetworkClient {

    private const val AZURE_BASE_URL         = "https://dev.azure.com"
    private const val VISUAL_STUDIO_BASE_URL = "https://vssps.dev.azure.com"
    private const val GITHUB_BASE_URL        = "https://api.github.com"
    private const val GITLAB_BASE_URL        = "https://gitlab.com/api/v4"
    private const val AZURE_VERSION          = "5.0"
    private const val VISUAL_STUDIO_VERSION  = "5.0-preview.1"

    private fun createOkHttpClient(interceptor: (Interceptor.Chain) -> Response) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    fun createAzureService(namespace: String, project: String, token: String): AzureService {
        val retrofit = Retrofit.Builder()
            .client(createOkHttpClient { chain ->
                val original = chain.request()
                val originalUrl = original.url()

                val url = originalUrl.newBuilder()
                    .addQueryParameter("api-version", AZURE_VERSION)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                    .addHeader("Authorization", token)

                val request = requestBuilder.build()
                return@createOkHttpClient chain.proceed(request)
            }).baseUrl("$AZURE_BASE_URL/$namespace/$project/_apis/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        return retrofit.create(AzureService::class.java)
    }

    fun createVisualStudioService(namespace: String, token: String): VisualStudioService {
        val retrofit = Retrofit.Builder()
            .client(createOkHttpClient { chain ->
                val original = chain.request()
                val originalUrl = original.url()

                val url = originalUrl.newBuilder()
                    .addQueryParameter("api-version", VISUAL_STUDIO_VERSION)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                    .addHeader("Authorization", token)

                val request = requestBuilder.build()
                return@createOkHttpClient chain.proceed(request)
            }).baseUrl("$VISUAL_STUDIO_BASE_URL/$namespace/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        return retrofit.create(VisualStudioService::class.java)
    }

    fun createGitHubService(namespace: String, project: String, token: String): GithubService {
        val retrofit = Retrofit.Builder()
            .client(createOkHttpClient { chain ->
                val original = chain.request()
                val originalUrl = original.url()

                val url = originalUrl.newBuilder().build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                    .addHeader("Authorization", "token $token")

                val request = requestBuilder.build()
                return@createOkHttpClient chain.proceed(request)
            }).baseUrl("$GITHUB_BASE_URL/repos/$namespace/$project/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        return retrofit.create(GithubService::class.java)
    }

    fun createGitLabService(project: Int, token: String): GitlabService {
        val retrofit = Retrofit.Builder()
            .client(createOkHttpClient { chain ->
                val original = chain.request()
                val originalUrl = original.url()

                val url = originalUrl.newBuilder().build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                    .addHeader("Private-Token", token)

                val request = requestBuilder.build()
                return@createOkHttpClient chain.proceed(request)
            }).baseUrl("$GITLAB_BASE_URL/projects/$project/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        return retrofit.create(GitlabService::class.java)
    }
}