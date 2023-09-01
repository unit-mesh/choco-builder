package cc.unitmesh.cf.domains.frontend.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import cc.unitmesh.cf.domains.frontend.model.ComponentDsl
import cc.unitmesh.cf.domains.frontend.model.LayoutStyle
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import java.nio.file.Files.walk
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.isRegularFile

@Component
class FEVariableResolver : VariableResolver<FEVariables> {

    override fun resolve(): FEVariables {
        // load from resources
        val dir = this.javaClass.getResource("/frontend/components").path

        // walk through the dir and load all components
        val components: List<ComponentDsl> = walk(Path(dir))
            .filter { it.isRegularFile() && it.extension == "json" }
            .map { it.toFile().readText() }
            .map { Json.decodeFromString<ComponentDsl>(it) }
            .toList()

        val layoutDir = this.javaClass.getResource("/frontend/layout").path
        val layouts: List<LayoutStyle> = walk(Path(layoutDir))
            .filter { it.isRegularFile() && it.extension == "json" }
            .map { it.toFile().readText() }
            .map { Json.decodeFromString<List<LayoutStyle>>(it) }
            .toList()
            .flatten()

        return FEVariables(
            layouts = layouts.joinToString(separator = ",") { it.name },
            components = components.joinToString(separator = ",") {
                it.name + "(" + it.tagName + ")"
            }
        )
    }

    override fun compile(input: String): String {
        TODO("Not yet implemented")
    }
}

data class FEVariables(
    val layouts: String,
    val components: String,
)