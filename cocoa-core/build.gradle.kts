plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)
    implementation(kotlin("reflect"))
    implementation(libs.reflections)

    implementation(libs.velocity.engine)

    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}