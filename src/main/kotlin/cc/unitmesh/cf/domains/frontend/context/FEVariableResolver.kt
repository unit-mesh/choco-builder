package cc.unitmesh.cf.domains.frontend.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import cc.unitmesh.cf.domains.frontend.model.LayoutStyle
import cc.unitmesh.cf.domains.frontend.model.UiComponent
import kotlinx.serialization.json.Json
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.io.StringWriter
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.Files.walk
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile


@Component
class FEVariableResolver : VariableResolver<FEVariables> {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(FEVariableResolver::class.java)
    }
    override var variables: FEVariables? = FEVariables(
        question = "",
        histories = listOf(),
        layouts = "",
        components = "",
    )

    override val velocityContext = VelocityContext()

    val componentList: MutableList<UiComponent> = mutableListOf()

    init {
        try {
            this.resolve()
        } catch (e: Exception) {
            log.error("Failed to resolve variables", e)
        }
    }

    override fun resolve(question: String) {
        // walk through the dir and load all components
        val componentJsons = getFolderJson("/frontend/components")
        val components: List<UiComponent> = componentJsons
            .map { Json.decodeFromString<UiComponent>(it) }
            .toList()

        componentList.clear()
        componentList.addAll(components)

        // walk through the dir and load all layouts
        val layoutJson = getFolderJson("/frontend/layout")

        val layouts: List<LayoutStyle> =layoutJson
            .map { Json.decodeFromString<List<LayoutStyle>>(it) }
            .toList()
            .flatten()

        variables = FEVariables(
            question = question,
            histories = listOf(),
            layouts = layouts.joinToString(separator = ",") { it.name },
            components = components.joinToString(separator = ",") {
                it.name + "(" + it.tagName + ")"
            }
        )
    }

    private fun getFolderJson(path: String): List<String> {
        val results = mutableListOf<String>()

        val jarFile = File(javaClass.getProtectionDomain().codeSource.location.path)
        if (jarFile.isFile()) {
            val jar = JarFile(jarFile)
            val entries: Enumeration<JarEntry> = jar.entries()
            while (entries.hasMoreElements()) {
                val name: String = entries.nextElement().name
                if (name.startsWith(path)) {
                    val file = jarFile.inputStream().bufferedReader().use { it.readText() }
                    results.add(file)
                }
            }
            jar.close()
        } else { // Run with IDE
            val url: URL? = FEVariables::class.java.getResource(path)
            if (url != null) {
                try {
                    val apps = File(url.toURI())
                    for (app in apps.listFiles()!!) {
                        if (app.isFile) {
                            results.add(app.readText())
                        }
                    }
                } catch (ex: URISyntaxException) {
                    // never happens
                }
            }
        }

        return results
    }

    fun updateQuestion(question: String) {
        variables = variables!!.copy(question = question)
    }

    fun histories(histories: List<String>) {
        variables = variables!!.copy(histories = histories)
    }

    override fun compile(input: String): String {
        velocityContext.put("question", variables!!.question)
        velocityContext.put("layouts", variables!!.layouts)
        velocityContext.put("components", variables!!.components)
        velocityContext.put("histories", variables!!.histories)

        val sw = StringWriter()
        Velocity.evaluate(velocityContext, sw, "#" + this.javaClass.name, input)
        return sw.toString()
    }

    fun getComponents(): MutableList<UiComponent> {
        return componentList
    }
}
