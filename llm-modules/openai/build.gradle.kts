plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation("io.reactivex.rxjava3:rxjava:3.1.7")

    implementation(libs.bundles.openai)
    implementation(libs.bundles.jackson)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
