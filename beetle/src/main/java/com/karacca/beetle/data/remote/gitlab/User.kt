package com.karacca.beetle.data.remote.gitlab

import com.google.gson.annotations.SerializedName

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("username") val username: String,
    @SerializedName("avatar_url") val avatar: String
)