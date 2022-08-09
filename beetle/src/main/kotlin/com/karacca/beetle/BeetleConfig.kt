package com.karacca.beetle

import androidx.core.os.bundleOf

/**
 * @author karacca
 * @date 9.08.2022
 */

class BeetleConfig {
    private val extras = bundleOf()

    var enableAssignees = true
    var enableLabels = true

    internal fun extras() = extras

    fun key(key: String, value: String) {
        extras.putString(key, value)
    }

    fun key(key: String, value: Boolean) {
        extras.putBoolean(key, value)
    }

    fun key(key: String, value: Double) {
        extras.putDouble(key, value)
    }

    fun key(key: String, value: Float) {
        extras.putFloat(key, value)
    }

    fun key(key: String, value: Int) {
        extras.putInt(key, value)
    }
}
