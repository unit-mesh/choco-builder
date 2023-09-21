plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation("net.pwall.json:json-kotlin-schema:0.41")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
