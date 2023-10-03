package cc.unitmesh.prompt.validate;

import cc.unitmesh.docs.SampleCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MarkdownCodeBlockValidatorTest {

    @Test
    @SampleCode
    fun should_return_true_when_input_has_non_blank_text() {
        // given
        val input = "```kotlin\nval x = 10\n```"
        val validator = MarkdownCodeBlockValidator(input)

        // when
        val result = validator.validate()

        // then
        assertTrue(result)
    }

    @Test
    fun should_return_false_when_input_has_blank_text() {
        // given
        val input = "```kotlin\n\n```"
        val validator = MarkdownCodeBlockValidator(input)

        // when
        val result = validator.validate()

        // then
        assertFalse(result)
    }

    @Test
    fun should_return_false_when_input_has_no_text() {
        // given
        val input = "```kotlin\n```"
        val validator = MarkdownCodeBlockValidator(input)

        // when
        val result = validator.validate()

        // then
        assertFalse(result)
    }
}