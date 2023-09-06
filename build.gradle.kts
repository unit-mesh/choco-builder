import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    group = "cc.unitmesh"
    version = "0.0.3"

    repositories {
        mavenCentral()
        mavenLocal()
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
    implementation(projects.cocoaLocalEmbedding)
    implementation(projects.cocoaCore)
    implementation(projects.codeInterpreter)

    // kotlin dependencies
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(libs.serialization.json)
    implementation(libs.coroutines.core)

    // spring dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // spring dev dependencies
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // llm dependencies
    implementation(libs.azure.openai)
    implementation(libs.bundles.openai)

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
