package cc.unitmesh.cf.presentation

import cc.unitmesh.code.interpreter.KotlinInterpreter
import cc.unitmesh.code.interpreter.api.InterpreterRequest
import cc.unitmesh.code.messaging.Message
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/interpreter")
class InterpreterController {
    val interpreter = KotlinInterpreter()

    @PostMapping("/eval")
    fun eval(@RequestBody request: CodeRequest): Message {
        return interpreter.eval(
            InterpreterRequest(code = request.code)
        )
    }
}

data class CodeRequest(
    val code: String,
)
