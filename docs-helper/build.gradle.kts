@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("application")
    alias(libs.plugins.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.serialization)
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-compiler:1.9.10")

    implementation(libs.clikt)
    implementation(libs.kotlin.reflect)

    testImplementation(libs.bundles.test)
}

application {
    mainClass.set("cc.unitmesh.docs.RunnerKt")
}

tasks {
    shadowJar {
        manifest {
            attributes(Pair("Main-Class", "cc.unitmesh.docs.RunnerKt"))
        }
    }
}
