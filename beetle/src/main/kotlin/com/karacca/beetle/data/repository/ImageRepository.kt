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
