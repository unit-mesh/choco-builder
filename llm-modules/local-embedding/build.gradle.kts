@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.coroutines.core)

    implementation(libs.onnxruntime)
    implementation(libs.huggingface.tokenizers) {
        exclude(group = "org.slf4j", module = "slf4j-api")
    }

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
