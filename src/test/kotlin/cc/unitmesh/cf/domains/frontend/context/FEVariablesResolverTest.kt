package cc.unitmesh.cf.domains.frontend.context

import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.Test

class FEVariablesResolverTest {

    @Test
    fun should_get_all_variables() {
        val resolver = FEVariableResolver()
        val variables = resolver.resolve()

        variables.components.length shouldBeGreaterThan 1
        variables.layouts.length shouldBeGreaterThan 1

        println(variables)
    }
}