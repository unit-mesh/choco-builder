@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("antlr")
    java
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

    implementation("co.elastic.clients:elasticsearch-java:8.10.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("org.slf4j:slf4j-api:2.0.7")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
