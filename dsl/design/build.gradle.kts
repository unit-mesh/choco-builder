@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    java
    id("antlr")
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")

    implementation("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    implementation(libs.kotlin.stdlib)
    implementation(libs.serialization.json)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}

sourceSets.main {
    java.srcDirs("${project.buildDir}/generated-src")
}

tasks.generateGrammarSource {
    maxHeapSize = "64m"
    arguments = arguments + listOf("-package", "cc.unitmesh.dsl") + listOf("-visitor", "-long-messages")
    outputDirectory = file("${project.buildDir}/generated-src/cc/unitmesh/dsl")
}

tasks.withType<AntlrTask> {

}

tasks.withType<Jar>().configureEach {
    dependsOn(tasks.withType<AntlrTask>())
}

tasks.named("compileKotlin") {
    dependsOn(tasks.withType<AntlrTask>())
}