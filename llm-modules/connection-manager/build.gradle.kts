plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.openai.gpt3)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
