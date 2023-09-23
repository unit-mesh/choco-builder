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

    implementation("co.elastic.clients:elasticsearch-java:8.10.0")
    implementation(libs.jackson.databind)
    implementation(libs.slf4j.api)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
