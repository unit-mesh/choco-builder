plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)
    implementation(libs.serialization.json)
    implementation(libs.jtokkit)

    implementation(libs.gson)
    implementation(libs.rxjava3)

    implementation(kotlin("reflect"))
    implementation(libs.reflections)

    implementation(libs.velocity.engine)
    implementation(libs.jackson.module.kotlin)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}