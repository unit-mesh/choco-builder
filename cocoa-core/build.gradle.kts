plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)
    implementation(libs.serialization.json)

    implementation(kotlin("reflect"))
    implementation(libs.reflections)

    implementation(libs.velocity.engine)
    implementation(libs.jackson.module.kotlin)

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}