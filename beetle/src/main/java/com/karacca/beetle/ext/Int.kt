package com.karacca.beetle.ext

import android.content.res.Resources

/**
 * @user: omerkaraca
 * @date: 2019-07-15
 */

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()