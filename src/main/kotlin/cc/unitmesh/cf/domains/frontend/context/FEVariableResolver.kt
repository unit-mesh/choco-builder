package cc.unitmesh.cf.domains.frontend.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import cc.unitmesh.cf.domains.frontend.model.UiComponent
import cc.unitmesh.cf.domains.frontend.model.LayoutStyle
import kotlinx.serialization.json.Json
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.springframework.stereotype.Component
import java.io.StringWriter
import java.nio.file.Files.walk
import java.nio.file.Paths
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile

@Component
class FEVariableResolver : VariableResolver<FEVariables> {
    override var variables: FEVariables? = null
    override val velocityContext = VelocityContext()

    init {
        this.resolve()
    }

    override fun resolve(question: String) {
        // walk through the dir and load all components
        val resourceUrl = this.javaClass.getResource("/frontend/components")!!
        val dir = Paths.get(resourceUrl.toURI())

        val components: List<UiComponent> = walk(dir)
            .filter { it.isRegularFile() && it.extension == "json" }
            .map { it.toFile().readText() }
            .map { Json.decodeFromString<UiComponent>(it) }
            .toList()

        // walk through the dir and load all layouts
        val layoutUrl = this.javaClass.getResource("/frontend/layout")!!
        val layoutDir = Paths.get(layoutUrl.toURI())

        val layouts: List<LayoutStyle> = walk(layoutDir)
            .filter { it.isRegularFile() && it.extension == "json" }
            .map { it.toFile().readText() }
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
}

data class FEVariables(
    val question: String,
    val histories: List<String>,
    val layouts: String,
    val components: String,
)