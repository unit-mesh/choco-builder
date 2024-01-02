plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.llmModules.openai)

    implementation(libs.serialization.json)
    implementation(libs.rxjava3)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
