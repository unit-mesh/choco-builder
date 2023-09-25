package cc.unitmesh.prompt;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PromptManagerTests {
    private val manager = PromptManager()

    @Test
    fun should_return_prompt_when_getPrompt_called() {
        val data = javaClass.getResource("/testdata/sample.json")!!.toURI()
        val template = javaClass.getResource("/ui-clarify.open_ai.vm")!!.toURI()

        val output = manager.compile(template.path, data.path)

        output shouldBe """system:
            |You are a helpful assistant.
            |
            |user:
            |中国的首都是哪里？
            |assistant:
            |北京
            |
            |user:
            |中国的首都是哪里？
            |""".trimMargin()
    }
}