package cc.unitmesh.cf.presentation

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslCompiler
import cc.unitmesh.cf.core.dsl.DslInterpreter
import cc.unitmesh.dsl.design.DesignAppListener
import cc.unitmesh.dsl.design.DesignDsl
import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.Serializable
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/dsl")
class DslController(val dslCompiler: DesignCompiler) {
    @PostMapping("/{dslName}")
    fun lookupDsl(@PathVariable dslName: DslName, @RequestBody dslRequest: DslRequest): DslOutput {
        return when (dslName) {
            DslName.Design -> {
                dslCompiler.compile(SimpleCompilerDsl(dslName, dslRequest.dsl))
            }
        }
    }
}

@Serializable
enum class DslName(@JsonValue val value: String) {
    Design("Design")
}

@Serializable
data class DslRequest(
    val dsl: String,
)

class SimpleCompilerDsl(dslName: DslName, content: String) : Dsl {
    override var domain: String = dslName.value
    override val content: String = content
    override var interpreters: List<DslInterpreter> = emptyList()
}

@Component
class DesignCompiler : DslCompiler {
    override fun compile(dsl: Dsl): DslOutput {
        val information = DesignDsl().analysis(dsl.content)

        return DslOutput(
            name = "Design",
            description = "Design DSL",
            output = information,
            dsl = dsl.content,
        )
    }
}

data class DslOutput(
    val name: String,
    val description: String,
    val output: Any,
    val dsl: Any,
)