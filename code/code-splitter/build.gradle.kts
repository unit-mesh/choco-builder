@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

    implementation(libs.chapi.domain)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
