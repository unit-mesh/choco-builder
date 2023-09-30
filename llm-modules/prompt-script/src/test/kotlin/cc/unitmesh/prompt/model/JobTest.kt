package cc.unitmesh.prompt.model;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JobTest {

    @Test
    fun `should return list of validators`() {
        // given
        val input = "{}"
        val validateItems: List<ValidateItem> = listOf(
            ValidateItem.JsonItem("")
        )

        val job = Job(
            description = "test",
            template = "test",
            validate = validateItems
        )

        // when
        val result = job.buildValidtors(input)

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun should_return_true_when_run_json_validator() {
        // given
        val input = "{\"test\": \"test\"}"
        val validateItems: List<ValidateItem> = listOf(
            ValidateItem.JsonItem(""),
            ValidateItem.JsonPathItem("$.test")
        )

        val job = Job(
            description = "test",
            template = "test",
            validate = validateItems
        )

        // when
        val result = job.buildValidtors(input).map { it.validate() }

        // then
        assertEquals(2, result.size)
        assertEquals(true, result[0])
        assertEquals(true, result[1])
    }
}
