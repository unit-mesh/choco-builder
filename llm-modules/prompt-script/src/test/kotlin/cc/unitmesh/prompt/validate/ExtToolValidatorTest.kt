package cc.unitmesh.prompt.validate;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExtToolValidatorTest {
    @Test
    fun should_return_true_when_command_executes_successfully() {
        // given
        val execCommand = "ls -l"
        val input = ""
        val options = mapOf<String, String>()

        // when
        val result = ExtToolValidator(execCommand, input, options).validate()

        // then
        assertEquals(true, result)
    }

    @Test
    fun should_return_true_when_command_executes_successfully_with_input() {
        // given
        val execCommand = "ls -l"
        val input = "src"
        val options = mapOf<String, String>()

        // when
        val result = ExtToolValidator(execCommand, input, options).validate()

        // then
        assertEquals(true, result)
    }
}
