package cc.unitmesh.prompt.validate;

import cc.unitmesh.docs.SampleCode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class JsonPathValidatorTest {
    @Test
    @SampleCode
    fun should_return_true_when_path_is_valid() {
        // given
        val json = "{\"name\": \"John\", \"age\": 30}"
        val path = "$.name"

        // when
        val validator = JsonPathValidator(path, json)
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
        val validator = JsonPathValidator(path, json)
        val result = validator.validate()

        // then
        result shouldBe false
    }
}