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

    implementation("org.jsoup:jsoup:1.16.1")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
