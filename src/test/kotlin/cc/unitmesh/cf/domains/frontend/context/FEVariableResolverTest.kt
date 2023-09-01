package cc.unitmesh.cf.domains.frontend.context

import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class FEVariableResolverTest {

    @Test
    fun should_get_all_variables() {
        val resolver = FEVariableResolver()
        resolver.resolve("question")

        val compile = resolver.compile("""layouts: ${'$'}{layouts}""")
        compile.length shouldBeGreaterThan 0
    }

    @Test
    fun should_put_addition_variables() {
        val resolver = FEVariableResolver()
        resolver.resolve("question")

        resolver.put("test", "test")

        val compile = resolver.compile("""test: ${'$'}{test}""")
        compile shouldBe "test: test"
    }
}