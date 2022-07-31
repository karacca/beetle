package com.karacca.beetle.network

import com.karacca.beetle.network.model.Image
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * @author karacca
 * @date 28.07.2022
 */

internal interface ImageService {

    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part("key") key: RequestBody,
        @Part image: MultipartBody.Part
    ): Image
}
