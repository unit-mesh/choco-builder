plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.jupyter)
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.core)

    implementation(libs.jupyter.api)
    implementation(libs.jupyter.kernel)

    implementation(libs.bundles.letsPlot)

    // tips: don't add follow deps to project will cause issues
    compileOnly("org.jetbrains.kotlin:kotlin-scripting-jvm")

    testImplementation(libs.bundles.test)
}
