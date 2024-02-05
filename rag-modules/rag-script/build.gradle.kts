@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    id("org.jetbrains.kotlinx.dataframe") version "0.11.1"
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(projects.ragModules.document)
    implementation(projects.ragModules.storeElasticsearch)
    implementation(projects.codeModules.codeSplitter)
    implementation(projects.llmModules.localEmbedding)
    implementation(projects.llmModules.openai)

    implementation(libs.chapi.domain)

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

    implementation(libs.huggingface.tokenizers)
    implementation(libs.dataframe)

    implementation(libs.dotenv.kotlin)

    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)

    implementation(libs.rxjava3)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
