package cc.unitmesh.cf.core.context.variable

import org.apache.velocity.VelocityContext

class BasicVariableResolver : VariableResolver<String> {
    override var variables: String? = ""
    override val velocityContext = VelocityContext()

    override fun resolve(question: String) {

    }

    override fun compile(input: String): String {
        return ""
    }
}