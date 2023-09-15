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

    implementation("io.pinecone:pinecone-client:0.2.3")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
