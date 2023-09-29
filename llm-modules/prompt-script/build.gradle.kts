@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(projects.llmModules.connectionManager)

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)
    implementation(libs.jsonpath)

    implementation(libs.kaml)

    implementation(libs.velocity.engine)
    implementation(libs.gson)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
