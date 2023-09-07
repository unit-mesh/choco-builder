package cc.unitmesh.cf.domains.code

import cc.unitmesh.cf.core.dsl.Dsl
import cc.unitmesh.cf.core.dsl.Interpreter
import cc.unitmesh.cf.domains.SupportedDomains
import cc.unitmesh.code.interpreter.KotlinInterpreter
import cc.unitmesh.code.interpreter.api.InterpreterRequest
import cc.unitmesh.code.messaging.Message
import org.springframework.stereotype.Component

@Component
class CodeInterpreter : Interpreter {
    val repl: KotlinInterpreter = KotlinInterpreter()
    override fun canInterpret(dsl: Dsl): Boolean {
        return dsl.domain == SupportedDomains.CodeInterpreter.value
    }

    override fun interpret(dsl: Dsl): Message {
        return repl.eval(InterpreterRequest(code = dsl.content))
    }
}