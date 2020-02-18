package com.karacca.beetle.data.remote.github

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-12
 */

data class Label(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,
    @SerializedName("default") val default: Boolean
)