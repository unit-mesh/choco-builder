@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.clikt)

    implementation(projects.cocoaCore)
    implementation(projects.llmModules.connection)
    implementation(projects.llmModules.openai)

    implementation(libs.kotlin.stdlib)
    implementation(libs.rxjava3)

    implementation(libs.serialization.json)
    // for json path construction
    implementation(libs.gson)
    implementation(libs.jsonpath)

    implementation(libs.kaml)
    implementation(libs.kotlinx.datetime)
    implementation(libs.velocity.engine)

    implementation(libs.logging.logback.classic)

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
