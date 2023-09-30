@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(projects.llmModules.connection)
    implementation(projects.llmModules.openai)

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)
    implementation(libs.jsonpath)

    implementation(libs.kaml)

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    implementation(libs.velocity.engine)
    implementation(libs.gson)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
