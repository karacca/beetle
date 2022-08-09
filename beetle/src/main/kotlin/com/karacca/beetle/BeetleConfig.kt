package com.karacca.beetle

/**
 * @author karacca
 * @date 9.08.2022
 */

class BeetleConfig {
    val extras = hashMapOf<String, Any>()
    var enableAssignees = true
    var enableLabels = true

    fun key(key: String, value: String) {
        extras[key] = value
    }

    fun key(key: String, value: Boolean) {
        extras[key] = value
    }

    fun key(key: String, value: Double) {
        extras[key] = value
    }

    fun key(key: String, value: Float) {
        extras[key] = value
    }

    fun key(key: String, value: Int) {
        extras[key] = value
    }
}
