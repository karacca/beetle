package com.karacca.beetle.data.repository

import android.net.Uri
import androidx.core.net.toFile
import com.karacca.beetle.BuildConfig
import com.karacca.beetle.data.model.Image
import com.karacca.beetle.data.service.ImageService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * @author karacca
 * @date 8.08.2022
 */
 
internal class ImageRepository {
    private val imageService = ImageService.newInstance()

    suspend fun uploadImage(screenshot: Uri): Image {
        val file = screenshot.toFile()
        return imageService.uploadImage(
            BuildConfig.FREE_IMAGE_API_KEY.toRequestBody(),
            MultipartBody.Part.createFormData(
                "source",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        )
    }
}
