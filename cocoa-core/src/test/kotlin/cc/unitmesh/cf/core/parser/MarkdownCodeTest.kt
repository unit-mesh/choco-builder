package cc.unitmesh.cf.core.parser;

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MarkdownCodeTest {

    @Test
    fun should_parseMarkdownCode_when_validContentProvided() {
        // given
        val content = """
            ```kotlin
            val message = "Hello, World!"
            println(message)
            ```
        """.trimIndent()

        // when
        val markdownCode = MarkdownCode.parse(content)

        // then
        assertEquals("kotlin", markdownCode.language)
        assertEquals("val message = \"Hello, World!\"\nprintln(message)", markdownCode.text)
    }

    @Test
    fun should_parseMarkdownCode_withEmptyLanguage_when_noLanguageSpecified() {
        // given
        val content = """
            ```
            val message = "Hello, World!"
            println(message)
            ```
        """.trimIndent()

        // when
        val markdownCode = MarkdownCode.parse(content)

        // then
        assertEquals("", markdownCode.language)
        assertEquals("val message = \"Hello, World!\"\nprintln(message)", markdownCode.text)
    }

    @Test
    fun should_parseMarkdownCode_withIncompleteCode_when_codeNotClosed() {
        // given
        val content = """
            ```kotlin
            val message = "Hello, World!"
            println(message)
        """.trimIndent()

        // when
        val markdownCode = MarkdownCode.parse(content)

        // then
        assertEquals("kotlin", markdownCode.language)
        assertEquals("val message = \"Hello, World!\"\nprintln(message)\n", markdownCode.text)
        assertEquals(false, markdownCode.isComplete)
    }

    @Test
    fun should_parseMarkdownCode_withTrimmedCode_when_codeHasLeadingAndTrailingWhitespace() {
        // given
        val content = """
            ```kotlin
            val message = "Hello, World!"
            println(message)
            ```
            """.trimIndent()

        // when
        val markdownCode = MarkdownCode.parse(content)

        // then
        assertEquals("kotlin", markdownCode.language)
        assertEquals("val message = \"Hello, World!\"\nprintln(message)", markdownCode.text)
    }

    @Test
    fun should_parseMarkdownCode_withTrimmedCode_when_codeHasLeadingAndTrailingWhitespaceAndNewLine() {
        // given
        val content = """
            val message = "Hello, World!"
            println(message)
        """.trimIndent()

        // when
        val markdownCode = MarkdownCode.parse(content)

        // then
        assertEquals("", markdownCode.language)
        assertEquals("val message = \"Hello, World!\"\nprintln(message)", markdownCode.text)
    }
}
