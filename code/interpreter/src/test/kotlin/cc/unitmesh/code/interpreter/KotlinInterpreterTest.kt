package cc.unitmesh.code.interpreter

import cc.unitmesh.code.interpreter.api.InterpreterRequest
import org.junit.jupiter.api.Test

class KotlinInterpreterTest {
    private val compiler = KotlinInterpreter()

    @Test
    internal fun should_catch_issue_for_errored_code() {
        val request: InterpreterRequest = InterpreterRequest(
            code = "val x = 3; y*2",
            language = "kotlin",
        )
        val res = compiler.eval(request)
    }
}