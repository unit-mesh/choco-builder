@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.clikt)

    implementation(projects.cocoaCore)
    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

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
