@file:Suppress("SpellCheckingInspection")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
    id("com.diffplug.spotless")
    id("maven-publish")
}

android {
    compileSdk = 32
    defaultConfig {
        minSdk = 21
        targetSdk = 32
        namespace = "com.karacca.beetle"
        aarMetadata { minCompileSdk = 21 }

        val localProperties = gradleLocalProperties(rootDir)
        buildConfigField(
            "String",
            "GITHUB_APP_ID",
            "\"${localProperties.getProperty("GITHUB_APP_ID")}\""
        )

        buildConfigField(
            "String",
            "FREE_IMAGE_API_KEY",
            "\"${localProperties.getProperty("FREE_IMAGE_API_KEY")}\""
        )
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.karacca"
            artifactId = "beetle"
            version = "2.0.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}


dependencies {
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("com.google.android.material:material:1.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.2")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("io.coil-kt:coil:2.1.0")

    api("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.71")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-orgjson:0.11.5") {
        exclude(
            group = "org.json",
            module = "json"
        )
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**/*.kt")
        ktlint("0.44.0").userData(mapOf("disabled_rules" to "no-wildcard-imports"))
        licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }

    format("kts") {
        target("**/*.kts")
        targetExclude("**/build/**/*.kts")
        licenseHeaderFile(
            rootProject.file("spotless/copyright.kts"),
            "(^(?![\\/ ]\\*).*$)"
        )
    }

    format("xml") {
        target("**/*.xml")
        targetExclude("**/build/**/*.xml")
        licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
    }
}
