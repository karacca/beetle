@file:Suppress("SpellCheckingInspection")

import org.jlleitschuh.gradle.ktlint.KtlintExtension

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
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.2.1")
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<KtlintExtension> {
        version.set("0.44.0")
        disabledRules.add("no-wildcard-imports")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
