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
        "org.jetbrains.letsPlot.geom.extras.*",
        "org.jetbrains.letsPlot.stat.*",
        "org.jetbrains.letsPlot.label.*",
        "org.jetbrains.letsPlot.scale.*",
        "org.jetbrains.letsPlot.facet.*",
        "org.jetbrains.letsPlot.sampling.*",
        "org.jetbrains.letsPlot.export.*",
        "org.jetbrains.letsPlot.tooltips.*",
        "org.jetbrains.letsPlot.annotations.*",
        "org.jetbrains.letsPlot.themes.theme",
        "org.jetbrains.letsPlot.themes.elementBlank",
        "org.jetbrains.letsPlot.themes.elementRect",
        "org.jetbrains.letsPlot.themes.elementLine",
        "org.jetbrains.letsPlot.themes.elementText",
        "org.jetbrains.letsPlot.themes.themeBW",
        "org.jetbrains.letsPlot.themes.themeGrey",
        "org.jetbrains.letsPlot.themes.themeLight",
        "org.jetbrains.letsPlot.themes.themeClassic",
        "org.jetbrains.letsPlot.themes.themeMinimal",
        "org.jetbrains.letsPlot.themes.themeMinimal2",
        "org.jetbrains.letsPlot.themes.themeNone",
        "org.jetbrains.letsPlot.themes.margin",
        "org.jetbrains.letsPlot.themes.flavorDarcula",
        "org.jetbrains.letsPlot.themes.flavorSolarizedLight",
        "org.jetbrains.letsPlot.themes.flavorSolarizedDark",
        "org.jetbrains.letsPlot.themes.flavorHighContrastLight",
        "org.jetbrains.letsPlot.themes.flavorHighContrastDark",
        "org.jetbrains.letsPlot.font.*",
        "org.jetbrains.letsPlot.coord.coordFixed",
        "org.jetbrains.letsPlot.coord.coordCartesian",
        "org.jetbrains.letsPlot.coord.coordMap",
        "org.jetbrains.letsPlot.coord.coordFlip",
        "org.jetbrains.letsPlot.pos.positionIdentity",
        "org.jetbrains.letsPlot.pos.positionStack",
        "org.jetbrains.letsPlot.pos.positionFill",
        "org.jetbrains.letsPlot.pos.positionDodge",
        "org.jetbrains.letsPlot.pos.positionDodgeV",
        "org.jetbrains.letsPlot.pos.positionNudge",
        "org.jetbrains.letsPlot.pos.positionJitter",
        "org.jetbrains.letsPlot.pos.positionJitterDodge",
        "org.jetbrains.letsPlot.bistro.corr.*",
        "org.jetbrains.letsPlot.bistro.qq.*",
        "org.jetbrains.letsPlot.bistro.joint.*",
        "org.jetbrains.letsPlot.bistro.residual.*",
        "org.jetbrains.letsPlot.intern.toSpec"
    ),

    dependencies = listOf(
        // should keep same version to gradle/libs.versions.toml
        "org.jetbrains.lets-plot:lets-plot-kotlin-kernel:4.4.2",
        "org.jetbrains.lets-plot:lets-plot-common:4.0.0",
        "org.jetbrains.lets-plot:lets-plot-image-export:4.0.0",
    )
)

fun extendLibraries(): LibraryResolver {
    val kotless = "kotless" to Json.encodeToString(KotlessLibDef)
    val ktor = "ktor" to Json.encodeToString(ktorLibDef)
    val letsPlot = "lets-plot" to Json.encodeToString(letPlotDef)

    return listOf(kotless, ktor, letsPlot).toLibraries()
}
