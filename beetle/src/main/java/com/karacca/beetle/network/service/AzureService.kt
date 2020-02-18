package com.karacca.beetle.network.service

import com.karacca.beetle.data.remote.azure.Attachment
import com.karacca.beetle.data.remote.azure.WorkItemComposer
import com.karacca.beetle.data.remote.azure.WorkItemResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

interface AzureService {

    @POST("wit/attachments")
    fun uploadPhoto(@Query("fileName") name: String,
                    @Body body: RequestBody): Observable<Attachment>

    @POST("wit/workitems")
    fun createWorkItem(@Header("Content-Type") contentType: String,
                       @Query("type") type: String,
                       @Body body: List<WorkItemComposer.Document>): Observable<WorkItemResponse>

}