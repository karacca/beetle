package com.karacca.beetle.network.service

import com.karacca.beetle.data.remote.github.IssueRequest
import com.karacca.beetle.data.remote.github.IssueResponse
import com.karacca.beetle.data.remote.github.Label
import com.karacca.beetle.data.remote.github.User
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

interface GithubService {

    @GET("collaborators")
    fun collaborators(): Observable<List<User>>

    @GET("labels")
    fun labels(): Observable<List<Label>>

    @POST("issues")
    fun issue(@Body request: IssueRequest): Observable<IssueResponse>

}