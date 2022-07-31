package com.karacca.beetle.data.model
import com.google.gson.annotations.SerializedName


/**
 * @author karacca
 * @date 28.07.2022
 */
 
data class Image(
    @SerializedName("image")
    val image: Image?,
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_txt")
    val statusTxt: String?,
    @SerializedName("success")
    val success: Success?
) {
    data class Image(
        @SerializedName("access_hotlink")
        val accessHotlink: String?,
        @SerializedName("access_html")
        val accessHtml: String?,
        @SerializedName("adult")
        val adult: String?,
        @SerializedName("cloud")
        val cloud: String?,
        @SerializedName("date_fixed_peer")
        val dateFixedPeer: String?,
        @SerializedName("description")
        val description: Any?,
        @SerializedName("display_height")
        val displayHeight: String?,
        @SerializedName("display_url")
        val displayUrl: String?,
        @SerializedName("display_width")
        val displayWidth: String?,
        @SerializedName("expiration")
        val expiration: String?,
        @SerializedName("extension")
        val extension: String?,
        @SerializedName("file")
        val `file`: File?,
        @SerializedName("filename")
        val filename: String?,
        @SerializedName("height")
        val height: String?,
        @SerializedName("how_long_ago")
        val howLongAgo: String?,
        @SerializedName("id_encoded")
        val idEncoded: String?,
        @SerializedName("image")
        val image: Image?,
        @SerializedName("is_animated")
        val isAnimated: Int?,
        @SerializedName("is_use_loader")
        val isUseLoader: Boolean?,
        @SerializedName("likes")
        val likes: String?,
        @SerializedName("likes_label")
        val likesLabel: String?,
        @SerializedName("medium")
        val medium: Medium?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("nsfw")
        val nsfw: Int?,
        @SerializedName("original_exifdata")
        val originalExifdata: Any?,
        @SerializedName("original_filename")
        val originalFilename: String?,
        @SerializedName("ratio")
        val ratio: Double?,
        @SerializedName("size")
        val size: Int?,
        @SerializedName("size_formatted")
        val sizeFormatted: String?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("thumb")
        val thumb: Thumb?,
        @SerializedName("time")
        val time: String?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("title_truncated")
        val titleTruncated: String?,
        @SerializedName("title_truncated_html")
        val titleTruncatedHtml: String?,
        @SerializedName("url")
        val url: String?,
        @SerializedName("url_seo")
        val urlSeo: String?,
        @SerializedName("url_short")
        val urlShort: String?,
        @SerializedName("url_viewer")
        val urlViewer: String?,
        @SerializedName("url_viewer_preview")
        val urlViewerPreview: String?,
        @SerializedName("url_viewer_thumb")
        val urlViewerThumb: String?,
        @SerializedName("views_hotlink")
        val viewsHotlink: String?,
        @SerializedName("views_html")
        val viewsHtml: String?,
        @SerializedName("views_label")
        val viewsLabel: String?,
        @SerializedName("vision")
        val vision: String?,
        @SerializedName("width")
        val width: String?
    ) {
        data class File(
            @SerializedName("resource")
            val resource: Resource?
        ) {
            data class Resource(
                @SerializedName("chain")
                val chain: Chain?,
                @SerializedName("chain_code")
                val chainCode: ChainCode?
            ) {
                data class Chain(
                    @SerializedName("image")
                    val image: String?,
                    @SerializedName("medium")
                    val medium: String?,
                    @SerializedName("thumb")
                    val thumb: String?
                )

                data class ChainCode(
                    @SerializedName("image")
                    val image: String?,
                    @SerializedName("medium")
                    val medium: String?,
                    @SerializedName("thumb")
                    val thumb: String?
                )
            }
        }

        data class Image(
            @SerializedName("extension")
            val extension: String?,
            @SerializedName("filename")
            val filename: String?,
            @SerializedName("mime")
            val mime: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("size")
            val size: Int?,
            @SerializedName("url")
            val url: String?
        )

        data class Medium(
            @SerializedName("extension")
            val extension: String?,
            @SerializedName("filename")
            val filename: String?,
            @SerializedName("mime")
            val mime: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )

        data class Thumb(
            @SerializedName("extension")
            val extension: String?,
            @SerializedName("filename")
            val filename: String?,
            @SerializedName("mime")
            val mime: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("url")
            val url: String?
        )
    }

    data class Success(
        @SerializedName("code")
        val code: Int?,
        @SerializedName("message")
        val message: String?
    )
}
