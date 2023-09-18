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
        try {
            interpreter.eval(
                InterpreterRequest(code = request.code)
            ).let {
                return it
            }
        } catch (e: Exception) {
            return Message(
                resultValue = e.stackTraceToString(),
                displayValue = e.stackTraceToString(),
                className = "",
                msgType = cc.unitmesh.code.messaging.MessageType.ERROR,
                content = cc.unitmesh.code.messaging.ErrorContent(
                    e.javaClass.name,
                    e.toString()
                )
            )
        }
    }
}

data class CodeRequest(
    val code: String,
)
