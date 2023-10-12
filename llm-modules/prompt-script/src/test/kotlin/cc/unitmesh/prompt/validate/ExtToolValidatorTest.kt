package cc.unitmesh.prompt.validate;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExtToolValidatorTest {
    @Test
    fun should_return_false_when_command_executes_unsuccessfully() {
        // Given
        val execCommand = "invalid_command"
        val input = "input"

        // When
        val validator = ExtToolValidator(execCommand, input, it.options)
        val result = validator.validate()

        // Then
        assertEquals(false, result)
    }
}
