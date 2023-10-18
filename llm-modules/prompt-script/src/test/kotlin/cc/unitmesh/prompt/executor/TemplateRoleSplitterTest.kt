package cc.unitmesh.prompt.executor;

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
}
