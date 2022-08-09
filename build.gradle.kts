@file:Suppress("SpellCheckingInspection")

buildscript {
    extra.apply {
        set("coreKtxVersion", "1.8.0")
        set("appCompatVersion", "1.4.2")
        set("materialVersion", "1.6.1")
    }

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.7.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
