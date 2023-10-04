package cc.unitmesh.prompt.validate;

import cc.unitmesh.docs.SampleCode
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class StringValidatorTest {

    @Test
    @SampleCode(name = "成功场景", content = "")
    fun string_eval() {
        // start-sample
        val expr1 = "output contains \"hello\""
        val expr2 = "output endsWith \"world\""
        val expr3 = "output startsWith \"hello\""
        val expr4 = "output == \"hello world\""
        val expr5 = "output == 'hello world'"
        val expr6 = "output.length > 5"
        // end-sample

        StringValidator(expr1, "hello world").validate() shouldBe true
        StringValidator(expr2, "hello world").validate() shouldBe true
        StringValidator(expr3, "hello world").validate() shouldBe true
        StringValidator(expr4, "hello world").validate() shouldBe true
        StringValidator(expr5, "hello world").validate() shouldBe true
        StringValidator(expr6, "hello, world").validate() shouldBe true
    }
}