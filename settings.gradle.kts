@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "chocolate-factory"

include(":cocoa-core")
include(":code-interpreter")
include(":cocoa-local-embedding")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

