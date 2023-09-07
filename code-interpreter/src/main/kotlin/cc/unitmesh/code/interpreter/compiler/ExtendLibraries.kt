package cc.unitmesh.code.interpreter.compiler

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.clickprompt.chatrepl.compiler.toLibraries
import org.jetbrains.kotlinx.jupyter.api.libraries.KernelRepository
import org.jetbrains.kotlinx.jupyter.libraries.LibraryResolver

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

val letPlotDef = SimpleLibraryDefinition(
    imports = listOf(
        "org.jetbrains.letsPlot.*",
        "org.jetbrains.letsPlot.geom.*",
        "org.jetbrains.letsPlot.stat.*",
        "org.jetbrains.letsPlot.label.*",
        "org.jetbrains.letsPlot.scale.*",
        "org.jetbrains.letsPlot.facet.*",
        "org.jetbrains.letsPlot.sampling.*",
        "org.jetbrains.letsPlot.export.*",
        "org.jetbrains.letsPlot.bistro.corr.CorrPlot",
        "org.jetbrains.letsPlot.tooltips.layerTooltips",
        "org.jetbrains.letsPlot.tooltips.tooltipsNone",
        "org.jetbrains.letsPlot.intern.toSpec"
    ),
    dependencies = listOf(
        "org.jetbrains.lets-plot:lets-plot-kotlin-kernel:4.4.2",
        "org.jetbrains.lets-plot:lets-plot-common:4.0.0",
        "org.jetbrains.lets-plot:lets-plot-image-export:4.0.0",
//        "io.github.microutils:kotlin-logging-jvm:2.0.5"
    )
)
fun extendLibraries(): LibraryResolver {
    val kotless = "kotless" to Json.encodeToString(KotlessLibDef)
    val ktor = "ktor" to Json.encodeToString(ktorLibDef)
    val letsPlot = "lets-plot" to Json.encodeToString(letPlotDef)

    return listOf(kotless, ktor, letsPlot).toLibraries()
}
