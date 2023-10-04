package cc.unitmesh.prompt.validate;

import cc.unitmesh.docs.SampleCode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class JsonPathValidatorTest {
    @Test
    @SampleCode(name = "检验成功", content = "")
    fun should_return_true_when_path_is_valid() {
        // start-sample
        val expression = "$.name"
        val input = "{\"name\": \"John\", \"age\": 30}"
        // end-sample

        // when
        val validator = JsonPathValidator(expression, input)
        val result = validator.validate()

        // then
        result shouldBe true
    }

    @Test
    @SampleCode(name = "校验失败", content = "")
    fun should_return_false_when_path_is_invalid() {
        // start-sample
        val expression = "$.address"
        val input = "{\"name\": \"John\", \"age\": 30}"
        // end-sample

        // when
        val validator = JsonPathValidator(expression, input)
        val result = validator.validate()

        // then
        result shouldBe false
    }
}