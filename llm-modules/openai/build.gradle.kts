plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(libs.rxjava3)
    implementation(libs.serialization.json)

    implementation(libs.bundles.openai)
    implementation(libs.coroutines.core)
    implementation(libs.bundles.jackson)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
