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
    implementation(projects.code.codeSplitter)
    implementation(projects.ragModules.document)
    implementation(projects.ragModules.storeElasticsearch)
    implementation(projects.llmModules.sentenceTransformers)
    implementation(projects.llmModules.openai)

    implementation(libs.chapi.domain)

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

    implementation(libs.huggingface.tokenizers)
    implementation(libs.dataframe)

    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    implementation("io.ktor:ktor-client-logging:2.3.4")
    implementation("io.ktor:ktor-client-core:2.3.4")
    implementation("io.ktor:ktor-client-cio:2.3.4")

    implementation("io.reactivex.rxjava3:rxjava:3.1.7")

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
