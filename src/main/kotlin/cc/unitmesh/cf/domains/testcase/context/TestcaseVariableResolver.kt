package cc.unitmesh.cf.domains.testcase.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import org.apache.velocity.VelocityContext
import org.springframework.stereotype.Component

@Component
class TestcaseVariableResolver : VariableResolver<TestcaseVariables> {
    override var variables: TestcaseVariables? = null
    override val velocityContext = VelocityContext()

    override fun resolve(question: String) {
        TODO("Not yet implemented")
    }

    override fun compile(input: String): String {
        TODO("Not yet implemented")
    }

}
