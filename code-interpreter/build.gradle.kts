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

    // Serverless Kotlin Language Binding
    implementation(libs.kotless.lang)
    implementation(libs.kotless.lang.local)

    // Serverless Kotlin Spring
    implementation(libs.kotless.spring.boot.lang)
    implementation(libs.kotless.spring.boot.lang.local)
    implementation(libs.kotless.spring.lang.parser)

    // Serverless Kotlin Ktor
    implementation(libs.kotless.ktor.lang)
    implementation(libs.kotless.ktor.lang.local)

    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.2")

    // tips: don't add follow deps to project will cause issues
    compileOnly("org.jetbrains.kotlin:kotlin-scripting-jvm")

    testImplementation(libs.bundles.test)
}
