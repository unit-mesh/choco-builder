plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    id("org.jetbrains.kotlin.jupyter.api") version "0.12.0-56"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

    implementation("org.jetbrains.kotlinx:kotlin-jupyter-api:0.12.0-56")
    implementation("org.jetbrains.kotlinx:kotlin-jupyter-kernel:0.12.0-56")

    implementation(libs.bundles.letsPlot)

    // tips: don't add follow deps to project will cause issues
    compileOnly("org.jetbrains.kotlin:kotlin-scripting-jvm")

    testImplementation(libs.bundles.test)
}
