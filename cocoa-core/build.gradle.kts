plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)
    implementation(libs.serialization.json)
    implementation(libs.jtokkit)

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.reactivex.rxjava3:rxjava:3.1.7")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    implementation(kotlin("reflect"))
    implementation(libs.reflections)

    implementation(libs.velocity.engine)
    implementation(libs.jackson.module.kotlin)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}