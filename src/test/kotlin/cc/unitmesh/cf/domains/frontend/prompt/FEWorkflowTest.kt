package cc.unitmesh.cf.domains.frontend.prompt

import org.junit.jupiter.api.Test

class FEWorkflowTest {

    @Test
    fun should_return_pure_text_for_testing_in_gui() {
        val clarifyPrompt = FEWorkflow().CLARIFY.systemPrompt
        println(clarifyPrompt)

        val analysePrompt = FEWorkflow().CLARIFY.forTestFormat()
        println(analysePrompt)
    }
}