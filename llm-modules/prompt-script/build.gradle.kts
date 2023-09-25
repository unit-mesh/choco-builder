@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)

    implementation(libs.velocity.engine)
    implementation(libs.dataframe)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
