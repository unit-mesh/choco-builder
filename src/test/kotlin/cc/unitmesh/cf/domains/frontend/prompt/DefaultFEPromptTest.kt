package cc.unitmesh.cf.domains.frontend.prompt

import org.junit.jupiter.api.Test

class DefaultFEPromptTest {

    @Test
    fun should_return_pure_text_for_testing_in_gui() {
        val template = DefaultFEPrompt.CLARIFY.template
        println(template)
    }
}