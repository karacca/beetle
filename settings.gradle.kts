pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "beetle"
include(":beetle")
include(":sample", ":sample-compose")
