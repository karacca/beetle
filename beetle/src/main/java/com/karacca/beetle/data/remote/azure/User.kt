package com.karacca.beetle.data.remote.azure

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

data class UserResponse(val count: Int, val value: List<User>)

data class User(
    @SerializedName("descriptor") val descriptor: String,
    @SerializedName("mailAddress") val mail: String,
    @SerializedName("displayName") val name: String,
    @SerializedName("_links") val links: LinkedTreeMap<String, Any>
){

    val avatar: String
        get() {
            @Suppress("UNCHECKED_CAST")
            return (links["avatar"] as? LinkedTreeMap<String, Any>)!!["href"] as String
        }
}