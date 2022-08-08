package com.karacca.beetle.utils

import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import androidx.core.os.bundleOf
import com.karacca.beetle.R
import java.lang.reflect.Field

/**
 * @author karacca
 * @date 7.08.2022
 */

internal object DeviceUtils {

    fun getDeviceData(context: Context): Bundle {
        return bundleOf(
            context.getString(R.string.brand) to Build.BRAND,
            context.getString(R.string.model) to Build.MODEL,
            context.getString(R.string.version_key) to context.getString(
                R.string.version_value,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT
            )
        )
    }
}
