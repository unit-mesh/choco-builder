import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"

    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jetbrains.kotlinx.dataframe") version "0.11.1"

    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.spring.io/snapshot")
        name = "Spring Snapshots"
    }
}

// allow scripts to unpack, when spring boot start, those packages will unpack to some dir, so we can call it REPL.
tasks.withType<BootJar> {
    requiresUnpack("**/kotlin-compiler-*.jar")
    requiresUnpack("**/kotlin-script-*.jar")
    requiresUnpack("**/kotlin-scripting-*.jar")
    requiresUnpack("**/kotlin-jupyter-*.jar")
    requiresUnpack("**/lets-plot-*.jar")
    requiresUnpack("**/dataframe-*.jar")
    requiresUnpack("**/kotlinx-*.jar")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    group = "cc.unitmesh"
    version = "0.2.3"

    repositories {
        mavenCentral()
        mavenLocal()
        maven(url = uri("https://packages.jetbrains.team/maven/p/ktls/maven"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    implementation(projects.llmModules.sentenceTransformers)
    implementation(projects.cocoaCore)
    implementation(projects.dsl.design)
    implementation(projects.code.interpreter)
    implementation(projects.code.codeSplitter)
    implementation(projects.ragModules.storeElasticsearch)

    // kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.serialization.json)
    implementation(libs.coroutines.core)

    implementation("io.reactivex.rxjava3:rxjava:3.1.7")

    // spring dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // spring dev dependencies
//    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // clone git repo
    compileOnly("org.archguard.codedb:action-toolkit:0.1.2")
    implementation("org.archguard.codedb:checkout:0.1.2")
    implementation(libs.chapi.domain)

    // llm dependencies
    implementation(libs.azure.openai)
    implementation(libs.bundles.openai)
    implementation(libs.huggingface.tokenizers)

    // data serialization dependencies
    implementation(libs.bundles.jackson)

    // data convert
    implementation(kotlin("reflect"))
    implementation(libs.reflections)
    implementation(libs.dataframe)

    // database dependencies
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation(libs.velocity.engine)

    // load config from .env file for testing
    testImplementation(libs.dotenv.kotlin)

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}
