package cc.unitmesh.prompt.validate;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AssertValidatorTest {

    @Test
    fun string_eval() {
        // contains
        AssertValidator("hello world", "output contains \"hello\"").validate() shouldBe true
        // endsWith
        AssertValidator("hello world", "output endsWith \"world\"").validate() shouldBe true
        // startsWith
        AssertValidator("hello world", "output startsWith \"hello\"").validate() shouldBe true
        // equal
        AssertValidator("hello world", "output == \"hello world\"").validate() shouldBe true
    }

    @Test
    fun int_and_float_eval() {
        AssertValidator("1.0", "output == 1").validate() shouldBe true
        AssertValidator("1.0", "output < 1.0").validate() shouldBe false
        AssertValidator("1.0", "output <= 1.0").validate() shouldBe true
        AssertValidator("1.0", "output > 1.0").validate() shouldBe false
        AssertValidator("1.0", "output >= 1.0").validate() shouldBe true
        AssertValidator("1.0", "output != 2.0").validate() shouldBe true
    }
}