package cc.unitmesh.prompt.model;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JobTest {

    @Test
    fun `should return list of validators`() {
        // given
        val input = "{}"
        val validateRules: List<ValidateRule> = listOf(
            ValidateRule.Json("")
        )

        val job = Job(
            description = "test",
            template = "test",
            validateRule = validateRules
        )

        // when
        val result = job.buildValidators(input)

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun should_return_true_when_run_json_validator() {
        // given
        val input = "{\"test\": \"test\"}"
        val validateRules: List<ValidateRule> = listOf(
            ValidateRule.Json(""),
            ValidateRule.JsonPath("$.test")
        )

        val job = Job(
            description = "test",
            template = "test",
            validateRule = validateRules
        )

        // when
        val result = job.buildValidators(input).map { it.validate() }

        // then
        assertEquals(2, result.size)
        assertEquals(true, result[0])
        assertEquals(true, result[1])
    }
}
