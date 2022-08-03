package com.karacca.beetle.data.service

import com.google.gson.Gson
import com.karacca.beetle.BuildConfig
import com.karacca.beetle.data.model.Image
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    companion object {
        private const val BASE_URL = "https://freeimage.host/api/1/"

        fun newInstance(): ImageService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
                .create(ImageService::class.java)
        }
    }
}
