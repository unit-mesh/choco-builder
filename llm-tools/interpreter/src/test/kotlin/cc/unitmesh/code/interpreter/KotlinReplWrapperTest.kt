package cc.unitmesh.code.interpreter

import cc.unitmesh.code.interpreter.compiler.KotlinReplWrapper
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KotlinReplWrapperTest {
    private lateinit var compiler: KotlinReplWrapper

    @BeforeEach
    internal fun setUp() {
        this.compiler = KotlinReplWrapper()
    }

    @Test
    internal fun simple_eval() {
        compiler.eval("val x = 3")
        val res = compiler.eval("x*2")
        res.rawValue shouldBe 6
    }

    @Test
    internal fun multiple_table() {
        compiler.eval("""fun generateMultiplicationTable(): String {
    val sb = StringBuilder()
    for (i in 1..9) {
        for (j in 1..9) {
            sb.append("'${'$'}{i * j}\t")
        }
        sb.append("\n")
    }
    return sb.toString()
}

val multiplicationTable = generateMultiplicationTable()
multiplicationTable""")
    }
}