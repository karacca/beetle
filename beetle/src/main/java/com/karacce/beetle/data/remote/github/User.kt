package com.karacce.beetle.data.remote.github

import com.google.gson.annotations.SerializedName


/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

data class User(
    @SerializedName("login") val login: String,
    @SerializedName("id") val id: Int,
    @SerializedName("avatar_url") val avatar: String
)