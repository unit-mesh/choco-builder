package cc.unitmesh.prompt.validate;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class AssertValidatorTest {

    @Test
    fun string_eval() {
        // contains
        AssertValidator("output contains \"hello\"", "hello world").validate() shouldBe true
        // endsWith
        AssertValidator("output endsWith \"world\"", "hello world").validate() shouldBe true
        // startsWith
        AssertValidator("output startsWith \"hello\"", "hello world").validate() shouldBe true
        // equal
        AssertValidator("output == \"hello world\"", "hello world").validate() shouldBe true
    }

    @Test
    fun int_and_float_eval() {
        AssertValidator("output == 1", "1.0").validate() shouldBe true
        AssertValidator("output < 1.0", "1.0").validate() shouldBe false
        AssertValidator("output <= 1.0", "1.0").validate() shouldBe true
        AssertValidator("output > 1.0", "1.0").validate() shouldBe false
        AssertValidator("output >= 1.0", "1.0").validate() shouldBe true
        AssertValidator("output != 2.0", "1.0").validate() shouldBe true
    }

    @Test
    @Ignore
    fun with_system_function() {
        AssertValidator("output.length > 5", "fafadf").validate() shouldBe true
    }
}