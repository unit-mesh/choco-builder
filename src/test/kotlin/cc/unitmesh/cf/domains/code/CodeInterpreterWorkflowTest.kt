package cc.unitmesh.cf.domains.code

import org.junit.jupiter.api.Test

class CodeInterpreterWorkflowTest {

    @Test
    fun should_return_continue_action_and_ask_content() {
        // when
        val result = CodeInterpreterWorkflow.EXECUTE.format()

        println(result)
    }
}