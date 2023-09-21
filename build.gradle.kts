import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)

    id("java-library")
    id("maven-publish")
    publishing
    signing
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "jacoco")

    group = "cc.unitmesh"
    version = "0.3.2"

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

configure(
    allprojects
            - project(":server")
            - project(":rag-modules")
            - project(":llm-modules")
            - project(":code")
            - project(":dsl")
) {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "publishing")

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set("ChocolateFactory")
                    description.set("Chocolate Factory is a cutting-edge LLM application engine designed to empower you in creating your very own AI assistant.")
                    url.set("https://framework.unitmesh.cc/")
                    licenses {
                        license {
                            name.set("MPL-2.0")
                            url.set("https://github.com/unit-mesh/chocolate-factory/raw/master/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("UnitMesh")
                            name.set("UnitMesh Team")
                            email.set("h@phodal.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/unit-mesh/chocolate-factory.git")
                        developerConnection.set("scm:git:ssh://github.com/unit-mesh/chocolate-factory.git")
                        url.set("https://github.com/unit-mesh/chocolate-factory/")
                    }
                }
            }
        }

        repositories {
            maven {
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

                credentials {
                    username =
                        (if (project.findProperty("sonatypeUsername") != null) project.findProperty("sonatypeUsername")
                        else System.getenv("MAVEN_USERNAME")).toString()
                    password =
                        (if (project.findProperty("sonatypePassword") != null) project.findProperty("sonatypePassword")
                        else System.getenv("MAVEN_PASSWORD")).toString()
                }
            }
        }
    }

    signing {
        sign(publishing.publications["mavenJava"])
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }
}
