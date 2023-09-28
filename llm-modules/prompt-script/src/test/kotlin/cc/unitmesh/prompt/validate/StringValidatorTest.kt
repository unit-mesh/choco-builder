package cc.unitmesh.prompt.validate;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StringValidatorTest {

    @Test
    fun string_eval() {
        StringValidator("output contains \"hello\"", "hello world").validate() shouldBe true
        StringValidator("output endsWith \"world\"", "hello world").validate() shouldBe true
        StringValidator("output startsWith \"hello\"", "hello world").validate() shouldBe true
        StringValidator("output == \"hello world\"", "hello world").validate() shouldBe true
        StringValidator("output == 'hello world'", "hello world").validate() shouldBe true

        // length
        StringValidator("output.length > 5", "fafadf").validate() shouldBe true
    }
}