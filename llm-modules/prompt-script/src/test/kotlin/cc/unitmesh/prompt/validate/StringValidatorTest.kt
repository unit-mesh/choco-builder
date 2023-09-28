package cc.unitmesh.prompt.validate;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class StringValidatorTest {

    @Test
    fun string_eval() {
        // contains
        StringValidator("output contains \"hello\"", "hello world").validate() shouldBe true
        // endsWith
        StringValidator("output endsWith \"world\"", "hello world").validate() shouldBe true
        // startsWith
        StringValidator("output startsWith \"hello\"", "hello world").validate() shouldBe true
        // equal
        StringValidator("output == \"hello world\"", "hello world").validate() shouldBe true
    }

    @Test
    fun int_and_float_eval() {
        StringValidator("output == 1", "1.0").validate() shouldBe true
        StringValidator("output < 1.0", "1.0").validate() shouldBe false
        StringValidator("output <= 1.0", "1.0").validate() shouldBe true
        StringValidator("output > 1.0", "1.0").validate() shouldBe false
        StringValidator("output >= 1.0", "1.0").validate() shouldBe true
        StringValidator("output != 2.0", "1.0").validate() shouldBe true
    }

    @Test
    @Ignore
    fun with_system_function() {
        StringValidator("output.length > 5", "fafadf").validate() shouldBe true
    }
}