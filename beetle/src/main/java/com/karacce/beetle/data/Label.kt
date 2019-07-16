package com.karacce.beetle.data

/**
 * @user: omerkaraca
 * @date: 2019-07-12
 */

data class Label(

    /**
     * Label name
     */
    val label: String,

    /**
     * Label Color
     */
    val color: String,

    /**
     * Label Descriptor
     */
    val descriptor: String,

    /**
     * State for adapters
     */
    var selected: Boolean = false

): Cell

data class LabelContainer(val items: List<Label>)