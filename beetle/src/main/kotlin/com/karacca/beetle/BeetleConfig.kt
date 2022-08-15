/*
 * Copyright 2022 Omer Karaca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.karacca.beetle

/**
 * @author karacca
 * @date 9.08.2022
 */

class BeetleConfig {
    val userAttributes = arrayListOf<Pair<String, Any>>()

    var organization: String? = null
    var repository: String? = null
    val initialized: Boolean
        get() = organization != null && repository != null

    var enableAssignees = true
    var enableLabels = true

    fun key(key: String, value: String) {
        userAttributes.add(key to value)
    }

    fun key(key: String, value: Boolean) {
        userAttributes.add(key to value)
    }

    fun key(key: String, value: Double) {
        userAttributes.add(key to value)
    }

    fun key(key: String, value: Float) {
        userAttributes.add(key to value)
    }

    fun key(key: String, value: Int) {
        userAttributes.add(key to value)
    }
}
