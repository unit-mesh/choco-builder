package cc.unitmesh.prompt;

import cc.unitmesh.prompt.template.TemplateCompilerFactory
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test

class TemplateCompilerFactoryTests {
    private val manager = TemplateCompilerFactory()

    @Test
    fun should_return_prompt_when_getPrompt_called() {
        val data = javaClass.getResource("/testdata/sample.json")!!.toURI()
        val template = javaClass.getResource("/testdata/ui-clarify.open_ai.vm")!!.toURI()

        val output = manager.compile(template.path, data.path)
        output shouldContain "中国的首都是哪里"
// TODO: spike why not working in Windows, since I don't have Windows development env.
//        output shouldBe """system:
//            |You are a helpful assistant.
//            |
//            |user:
//            |中国的首都是哪里？
//            |assistant:
//            |北京
//            |
//            |user:
//            |中国的首都是哪里？
//            |""".trimMargin()
    }
}