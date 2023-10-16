@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.cocoaCore)

    implementation(projects.codeModules.gitDiffer)
    implementation(libs.codedb.checkout)
    implementation(libs.archguard.analyser.diffChanges)

    implementation(projects.codeModules.codeSplitter)

    implementation(projects.llmModules.sentenceTransformers)
    implementation(projects.ragModules.storeElasticsearch)
    implementation(projects.ragModules.document)

    implementation(libs.kaml)
    implementation(libs.github.api)
    implementation(libs.gitlab4j.api)

    implementation(libs.clikt)

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)
    implementation(libs.logging.slf4j.api)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}


application {
    mainClass.set("cc.unitmesh.genius.MainKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "cc.unitmesh.genius.MainKt"))
        }
    }
}
