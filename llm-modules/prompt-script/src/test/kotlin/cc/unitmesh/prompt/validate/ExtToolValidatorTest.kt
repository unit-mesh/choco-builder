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
        val validator = ExtToolValidator(execCommand, input, emptyMap())
        val result = validator.validate()

        // Then
        assertEquals(false, result)
    }

    @Test
    fun should_validate_git_command_with_status_with_short_format() {
        // Given
        val execCommand = "git"
        val options = mapOf(
            "status" to ".",
        )

        // When
        val validator = ExtToolValidator(execCommand, "", options)
        val result = validator.validate()

        // Then
        assertEquals(true, result)
    }
}
