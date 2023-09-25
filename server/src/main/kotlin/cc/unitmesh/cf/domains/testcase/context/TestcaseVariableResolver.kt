package cc.unitmesh.cf.domains.testcase.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.springframework.stereotype.Component
import java.io.StringWriter

@Component
class TestcaseVariableResolver : VariableResolver<TestcaseVariables> {
    override var variables: TestcaseVariables? = TestcaseVariables(
        question = "",
        histories = listOf(),
    )
    override val velocityContext = VelocityContext()

    override fun resolve(question: String) {
        // TODO("Not yet implemented")
    }

    fun updateQuestion(question: String) {
        variables = variables!!.copy(question = question)
    }

    fun updateHistories(histories: List<String>) {
        variables = variables!!.copy(histories = histories)
    }

}
