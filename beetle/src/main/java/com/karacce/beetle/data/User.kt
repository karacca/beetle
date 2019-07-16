package com.karacce.beetle.data

/**
 * @user: omerkaraca
 * @date: 2019-07-11
 */

data class User(

    /**
     * User name to be displayed
     */
    val name: String,

    /**
     * User descriptor that refers a User.
     *
     * id(String) for: {Channel.AZURE, Channel.GITHUB}
     * id(Int) for: {Channel.GITLAB}
     */
    val descriptor: Any,

    /**
     * User avatar to be displayed
     */
    val avatar: String,

    /**
     * State for adapters
     */
    var selected: Boolean = false

): Cell

data class UserContainer(val items: List<User>)