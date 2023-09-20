@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    id("org.jetbrains.kotlinx.dataframe") version "0.11.1"
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

    implementation(libs.dataframe)

    implementation("io.ktor:ktor-client-logging:2.3.4")
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
