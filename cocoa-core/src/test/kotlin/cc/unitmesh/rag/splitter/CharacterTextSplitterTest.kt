package cc.unitmesh.rag.splitter;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CharacterTextSplitterTest {

    private val splitter = CharacterTextSplitter()

    @Test
    fun should_split_single_line() {
        val text = """paragraph 1
            |
            |paragraph 2
            |
            |paragraph 3
        """.trimMargin()

        val result = splitTextWithRegex(text, "\n\n", false)

        result.size shouldBe 3
        result shouldBe listOf("paragraph 1", "paragraph 2", "paragraph 3")
    }

    @Test
    fun should_split_multiple_paragraph() {
        val text = """paragraph 1
            |
            |paragraph 2
            |
            |paragraph 3
        """.trimMargin()

        val result = splitter.splitText(text)

        result[0] shouldBe text
    }
}