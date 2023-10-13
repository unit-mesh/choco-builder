@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.cocoaCore)
    implementation(projects.llmModules.connection)
    implementation(projects.llmModules.openai)

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)
    implementation(libs.jsonpath)
    implementation(libs.rxjava3)

    implementation(libs.logging.logback.classic)

    implementation(libs.kaml)

    implementation(libs.kotlinx.datetime)
    implementation(libs.clikt)

    implementation(libs.velocity.engine)
    implementation(libs.gson)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}

application {
    mainClass.set("cc.unitmesh.prompt.MainKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "cc.unitmesh.prompt.MainKt"))
        }
    }
}
