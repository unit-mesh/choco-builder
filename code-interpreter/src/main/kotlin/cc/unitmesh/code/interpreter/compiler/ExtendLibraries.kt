package cc.unitmesh.code.interpreter.compiler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.clickprompt.chatrepl.compiler.toLibraries
import org.jetbrains.kotlinx.jupyter.api.libraries.KernelRepository
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver


val mysqlLibDef = SimpleLibraryDefinition(
    imports = listOf("java.sql.*"),
    dependencies = listOf("mysql:mysql-connector-java:8.0.32")
)

val springLibDef = SimpleLibraryDefinition(
    imports = listOf(
        "org.springframework.boot.*",
        "org.springframework.boot.autoconfigure.*",
        "org.springframework.web.bind.annotation.*",
        "org.springframework.context.annotation.ComponentScan",
        "org.springframework.context.annotation.Configuration"
    ),
    dependencies = listOf(
        "org.springframework.boot:spring-boot-starter-web:2.7.9"
    )
)

val KotlessLibDef = SimpleLibraryDefinition(
    imports = listOf(
        "io.kotless.dsl.spring.*",
        "io.kotless.dsl.ktor.*",
        "io.kotless.dsl.lang.http.*",

        // todo: move to Kotlin libraries
        "kotlin.reflect.KClass",
        "kotlin.reflect.full.primaryConstructor"
    ),
    dependencies = listOf(
        "io.kotless:kotless-lang:0.2.0",
        "io.kotless:kotless-lang-local:0.2.0",
        "io.kotless:spring-boot-lang:0.2.0",
        "io.kotless:spring-boot-lang-local:0.2.0",
        "io.kotless:spring-lang-parser:0.2.0",
        "io.kotless:ktor-lang:0.2.0",
        "io.kotless:ktor-lang-local:0.2.0"
    ),
    repositories = listOf(
        "https://packages.jetbrains.team/maven/p/ktls/maven",
    ).map(::KernelRepository)
)

val ktorLibDef = SimpleLibraryDefinition(
    imports = listOf(
        "io.kotless.dsl.spring.*",
        "io.kotless.dsl.ktor.*",
        "io.ktor.application.*",
        "io.ktor.http.*",
        "io.ktor.request.*",
        "io.ktor.response.*",
        "io.ktor.routing.*",
        "io.ktor.server.engine.*",
        "io.ktor.server.netty.*",
        "io.kotless.dsl.lang.http.*",

        // todo: move to Kotlin libraries
        "kotlin.reflect.KClass",
        "kotlin.reflect.full.primaryConstructor"
    ),
    dependencies = listOf(
        "io.kotless:kotless-lang:0.2.0",
        "io.kotless:kotless-lang-local:0.2.0",
        "io.kotless:spring-boot-lang:0.2.0",
        "io.kotless:spring-boot-lang-local:0.2.0",
        "io.kotless:spring-lang-parser:0.2.0",
        "io.kotless:ktor-lang:0.2.0",
        "io.kotless:ktor-lang-local:0.2.0"
    ),
    repositories = listOf(
        "https://packages.jetbrains.team/maven/p/ktls/maven",
    ).map(::KernelRepository)
)

val exposedLibDef = SimpleLibraryDefinition(
    imports = listOf(
        "org.jetbrains.exposed.sql.*",
        "org.jetbrains.exposed.sql.transactions.*",
        "org.jetbrains.exposed.sql.SchemaUtils"
    ),
    dependencies = listOf(
        "org.jetbrains.exposed:exposed-core:0.40.1",
        "org.jetbrains.exposed:exposed-dao:0.40.1",
        "org.jetbrains.exposed:exposed-jdbc:0.40.1",
    )
)


fun extendLibraries(): LibraryResolver {
    val spring = "spring" to Json.encodeToString(springLibDef)
    val mysqlLibs = "mysql" to Json.encodeToString(mysqlLibDef)
    val kotless = "kotless" to Json.encodeToString(KotlessLibDef)
    val exposed = "exposed" to Json.encodeToString(exposedLibDef)
    val ktor = "ktor" to Json.encodeToString(ktorLibDef)

    return listOf(spring, mysqlLibs, kotless, exposed, ktor).toLibraries()
}
