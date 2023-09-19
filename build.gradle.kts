import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "jacoco")

    group = "cc.unitmesh"
    version = "0.2.3"

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://repo.spring.io/snapshot")
            name = "Spring Snapshots"
        }
        maven(url = uri("https://packages.jetbrains.team/maven/p/ktls/maven"))
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
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
