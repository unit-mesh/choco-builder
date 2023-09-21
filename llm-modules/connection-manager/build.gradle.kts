plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation("io.vertx:vertx-json-schema:4.4.5")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
