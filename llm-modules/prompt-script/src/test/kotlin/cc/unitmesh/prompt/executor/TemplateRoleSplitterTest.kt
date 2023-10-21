package cc.unitmesh.prompt.executor;

import cc.unitmesh.template.TemplateRoleSplitter
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TemplateRoleSplitterTest {

    private val splitter = TemplateRoleSplitter()

    @Test
    fun `should treat input without section header as user section`() {
        // given
        val input = "Hello, world!"

        // when
        val result = splitter.split(input)

        // then
        result.size shouldBe 1
        result["user"] shouldBe "Hello, world!"
    }


    @Test
    fun should_splitInputIntoSections() {
        // given
        val input = "```system```\nYou are a helpful assistant.\n\n```user```\n${'$'}{question}\n"
        val expectedSections = mapOf(
            "system" to "You are a helpful assistant.\n\n",
            "user" to "${'$'}{question}\n\n"
        )

        // when
        val sections = TemplateRoleSplitter().split(input)

        // then
        assertEquals(expectedSections, sections)
    }

    @Test
    fun should_handleRemainingContentAfterLastSection() {
        // given
        val input = "```system```\nYou are a helpful assistant.\n\n```user```\n${'$'}{question}\nRemaining content"
        val expectedSections = mapOf(
            "system" to "You are a helpful assistant.\n\n",
            "user" to "${'$'}{question}\nRemaining content\n"
        )

        // when
        val sections = TemplateRoleSplitter().split(input)

        // then
        assertEquals(expectedSections, sections)
    }

    @Test
    fun `should split input into sections`() {
        // given
        val input = """
            ```system```
            You are a helpful assistant.
            
            ```user```
            question
        """.trimIndent()

        // when
        val result = splitter.split(input)

        // then
        result.size shouldBe 2
        result["system"] shouldBe "You are a helpful assistant.\n\n"
        result["user"] shouldBe "question\n"
    }

    @Test
    fun `should handle for code hlight`() {
        val input = """```user```

你是一个资深的软件开发工程师，你擅长使用 TDD 的方式来开发软件，你现在需要帮助帮手开发人员做好 Tasking，以方便于编写测试用例。

${'$'}{frameworkContext}

当前类相关的代码如下：

```${'$'}{language}
${'$'}{beforeCursor}
```

请根据用户的输入，帮助
"""
        val result = splitter.split(input)
        result.size shouldBe 1
    }
}
