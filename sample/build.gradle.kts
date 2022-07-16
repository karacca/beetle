plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.karacca.sample"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0.0"
        vectorDrawables {
            useSupportLibrary = true
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

dependencies {
    implementation(project(":beetle"))
    implementation("androidx.core:core-ktx:${rootProject.extra.get("coreKtxVersion")}")
    implementation("androidx.appcompat:appcompat:${rootProject.extra.get("appCompatVersion")}")
    implementation("com.google.android.material:material:${rootProject.extra.get("materialVersion")}")
}
