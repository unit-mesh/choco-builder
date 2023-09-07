package cc.unitmesh.cf.domains.testcase.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.springframework.stereotype.Component
import java.io.StringWriter

@Component
class TestcaseVariableResolver : VariableResolver<TestcaseVariables> {
    override var variables: TestcaseVariables? = null
    override val velocityContext = VelocityContext()

    override fun resolve(question: String) {
        // TODO("Not yet implemented")
    }

    override fun compile(input: String): String {
        val sw = StringWriter()
        Velocity.evaluate(velocityContext, sw, "#" + this.javaClass.name, input)
        return sw.toString()
    }

    fun updateQuestion(question: String) {
        variables = variables!!.copy(question = question)
    }

}
