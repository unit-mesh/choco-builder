package cc.unitmesh.cf.domains.frontend.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import cc.unitmesh.cf.domains.frontend.model.LayoutStyle
import cc.unitmesh.cf.domains.frontend.model.UiComponent
import kotlinx.serialization.json.Json
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.springframework.stereotype.Component
import java.io.StringWriter


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
        val componentJsons = this.javaClass
            .getResourceAsStream("/frontend/components/mui-mini.json")!!
            .bufferedReader()
            .use { it.readText() }

        val components: List<UiComponent> = Json.decodeFromString(componentJsons)

        componentList.clear()
        componentList.addAll(components)

        // walk through the dir and load all layouts
        val layoutJson = this.javaClass
            .getResourceAsStream("/frontend/layouts/layout-style.json")!!
            .bufferedReader()
            .use { it.readText() }

        val layouts: List<LayoutStyle> = Json.decodeFromString<List<LayoutStyle>>(layoutJson)
        variables = FEVariables(
            question = question,
            histories = listOf(),
            layouts = layouts.joinToString(separator = ",") { it.name },
            components = components.map {
                it.components
            }
                .flatten()
                .distinct()
                .joinToString(separator = ",")
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

    fun getComponents(): MutableList<UiComponent> {
        return componentList
    }
}
