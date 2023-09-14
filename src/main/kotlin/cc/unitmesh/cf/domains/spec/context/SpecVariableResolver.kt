package cc.unitmesh.cf.domains.spec.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import cc.unitmesh.cf.domains.frontend.context.FEVariableResolver
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.slf4j.LoggerFactory.getLogger
import java.io.StringWriter

data class SpecVariables(
    val question: String,
    val domainWords: List<String>,
)

class SpecVariableResolver : VariableResolver<SpecVariables> {
    companion object {
        private val log = getLogger(SpecVariableResolver::class.java)
    }

    override val velocityContext = VelocityContext()

    override var variables: SpecVariables? = SpecVariables(
        question = "",
        domainWords = listOf(),
    )

    override fun compile(input: String): String {
        val sw = StringWriter()
        Velocity.evaluate(velocityContext, sw, "#" + this.javaClass.name, input)
        return sw.toString()
    }
}

