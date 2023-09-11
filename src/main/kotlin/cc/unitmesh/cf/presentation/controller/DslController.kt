package cc.unitmesh.cf.presentation.controller

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.DslInterpreter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dsl")
class DslController {
    @PostMapping("/{dslName}")
    fun lookupDsl(@PathVariable dslName: String): DslOutput {
        //
        return DslOutput("", "", "", object : Dsl {
            override var domain: String = ""
            override val content: String = ""
            override var interpreters: List<DslInterpreter> = emptyList()
        })
    }
}

data class DslOutput(
    val name: String,
    val description: String,
    val output: Any,
    val dsl: Any,
)