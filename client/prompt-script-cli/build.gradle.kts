plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}