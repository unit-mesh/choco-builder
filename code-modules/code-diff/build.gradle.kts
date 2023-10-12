@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    application
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(libs.serialization.json)
    implementation(libs.archguard.analyser.diffChanges)

    implementation(libs.chapi.domain)

    // should get the code data from language analyser
    implementation(libs.chapi.kotlin) {
        exclude(group = "com.ibm.icu", module = "icu4j")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }
    implementation(libs.chapi.java) {
        exclude(group = "com.ibm.icu", module = "icu4j")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-test-junit")
    }

    testImplementation(libs.logging.logback.classic)

    implementation(libs.jgit)
    testImplementation(libs.bundles.test)
}
