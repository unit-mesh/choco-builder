@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.serialization.json)

    implementation(libs.archguard.analyser.estimate)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
