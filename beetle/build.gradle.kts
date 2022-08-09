@file:Suppress("SpellCheckingInspection")

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        minSdk = 21
        targetSdk = 32

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

dependencies {
    implementation("androidx.core:core-ktx:${rootProject.extra.get("coreKtxVersion")}")
    implementation("androidx.appcompat:appcompat:${rootProject.extra.get("appCompatVersion")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    implementation("com.google.android.material:material:${rootProject.extra.get("materialVersion")}")

    val coroutinesVersion = "1.6.2"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

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
