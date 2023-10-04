package cc.unitmesh.prompt.validate;

import cc.unitmesh.docs.SampleCode
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MarkdownCodeBlockValidatorTest {

    @Test
    @SampleCode(name = "成功场景", content = "当代码块不为空里，返回 true。")
    fun should_return_true_when_input_has_non_blank_text() {
        // start-sample
        val input = "```kotlin\nval x = 10\n```"
        // end-sample

        val validator = MarkdownCodeBlockValidator(input)

        // when
        val result = validator.validate()

        // then
        assertTrue(result)
    }

    @Test
    @SampleCode(name = "成功场景", content = "当代码块为空时，返回 false。")
    fun should_return_false_when_input_has_blank_text() {
        // start-sample
        val input = "```kotlin\n\n```"
        // end-sample

        val validator = MarkdownCodeBlockValidator(input)

        // when
        val result = validator.validate()

        // then
        assertFalse(result)
    }
}