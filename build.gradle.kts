@file:Suppress("SpellCheckingInspection")

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
        classpath("com.android.tools.build:gradle:7.1.3")
        classpath("com.diffplug.spotless:spotless-plugin-gradle:6.7.1")
        classpath("com.vanniktech:gradle-maven-publish-plugin:0.21.0")
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
