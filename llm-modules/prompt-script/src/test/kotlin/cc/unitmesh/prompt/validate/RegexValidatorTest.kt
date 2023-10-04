package cc.unitmesh.prompt.validate;

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RegexValidatorTest {

    @Test
    fun shouldReturnTrueForValidInput() {
        // Given
        val regex = "\\d{4}" // Example regex pattern for 4 digits
        val input = "1234"
        val validator = RegexValidator(regex, input)

        // When
        val result = validator.validate()

        // Then
        assertTrue(result)
    }

    @Test
    fun shouldReturnFalseForInvalidInput() {
        // Given
        val regex = "\\d{4}" // Example regex pattern for 4 digits
        val input = "abc"
        val validator = RegexValidator(regex, input)

        // When
        val result = validator.validate()

        // Then
        assertFalse(result)
    }
}
