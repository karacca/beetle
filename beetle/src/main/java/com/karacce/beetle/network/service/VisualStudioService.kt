package com.karacce.beetle.network.service

import com.karacce.beetle.data.remote.azure.UserResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

interface VisualStudioService {

    @GET("_apis/graph/users")
    fun users(@Query("subjectTypes") subjectTypes: String): Observable<UserResponse>
}
