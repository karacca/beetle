package com.karacce.beetle.network.service

import com.karacce.beetle.data.remote.gitlab.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

interface GitlabService {

    @GET("members")
    fun members(): Observable<List<User>>

    @GET("labels")
    fun labels(): Observable<List<Label>>

    @Multipart
    @POST("uploads")
    fun upload(@Part file: MultipartBody.Part): Observable<Attachment>

    @POST("issues")
    fun issues(@Body request: IssueRequest): Observable<IssueResponse>

}