package cc.unitmesh.prompt.assert;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

class ExpressionTest {

    @Test
    @Ignore
    fun boolean_eval() {
        AssertExpr.eval("sample", "true && false") shouldBe false
        AssertExpr.eval("sample", "true && true") shouldBe true
    }

    @Test
    @Ignore
    fun string_eval() {
        // contains
        AssertExpr.eval("hello world", "output contains \"hello\"") shouldBe true
        // endsWith
        AssertExpr.eval("hello world", "output endsWith \"world\"") shouldBe true
        // startsWith
        AssertExpr.eval("hello world", "output startsWith \"hello\"") shouldBe true
        // equal
        AssertExpr.eval("hello world", "output == \"hello world\"") shouldBe true
    }

    @Test
    @Ignore
    fun int_and_float_eval() {
        AssertExpr.eval("1.0", "output == 1") shouldBe true
        AssertExpr.eval("1.0", "output < 1.0") shouldBe false
        AssertExpr.eval("1.0", "output <= 1.0") shouldBe true
        AssertExpr.eval("1.0", "output > 1.0") shouldBe false
        AssertExpr.eval("1.0", "output >= 1.0") shouldBe true
        AssertExpr.eval("1.0", "output != 2.0") shouldBe true
    }
}
