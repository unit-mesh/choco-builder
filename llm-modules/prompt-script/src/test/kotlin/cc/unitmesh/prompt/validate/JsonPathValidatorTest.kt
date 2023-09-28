package cc.unitmesh.prompt.validate;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class JsonPathValidatorTest {
    @Test
    fun should_return_true_when_path_is_valid() {
        // given
        val json = "{\"name\": \"John\", \"age\": 30}"
        val path = "$.name"

        // when
        val validator = JsonPathValidator(json, path)
        val result = validator.validate()

        // then
        result shouldBe true
    }

    @Test
    fun should_return_false_when_path_is_invalid() {
        // given
        val json = "{\"name\": \"John\", \"age\": 30}"
        val path = "$.address"

        // when
        val validator = JsonPathValidator(json, path)
        val result = validator.validate()

        // then
        result shouldBe false
    }
}