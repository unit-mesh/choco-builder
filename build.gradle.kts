import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"

    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.jetbrains.kotlinx.dataframe") version "0.11.1"

    // no use in correctly
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"

//    id("org.graalvm.buildtools.native") version "0.9.24"
}

group = "cc.unitmesh"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // spring dev dependencies
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // llm dependencies
    implementation("com.azure:azure-ai-openai:1.0.0-beta.3")
    implementation("com.theokanning.openai-gpt3-java:service:0.14.0")

    // data serialization dependencies
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-jsonSchema:2.14.2")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")

    // data convert
    implementation(kotlin("reflect"))
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlinx:dataframe:0.11.1")

    // database dependencies
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("org.apache.velocity:velocity-engine-core:2.3")

    // load config from .env file for testing
    testImplementation("io.github.cdimascio:dotenv-kotlin:6.4.1")

    // testing dependencies
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("io.kotest:kotest-assertions-core:5.6.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
