package cc.unitmesh.prompt.assert;

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ExpressionTest {

    @Test
    fun boolean_eval() {
        AssertExpr.eval("sample", "true") shouldBe false
        AssertExpr.eval("sample", "false") shouldBe false
        AssertExpr.eval("sample", "true && false") shouldBe false
        AssertExpr.eval("sample", "true && true") shouldBe true
    }

    @Test
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
}