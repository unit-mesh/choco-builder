package cc.unitmesh.cf.domains.semantic.context

import cc.unitmesh.cf.core.context.variable.VariableResolver
import cc.unitmesh.cf.domains.semantic.model.ExplainQuery
import cc.unitmesh.cf.domains.semantic.model.SemanticVariables
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter

class SemanticVariableResolver() : VariableResolver<SemanticVariables> {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(SemanticVariableResolver::class.java)
    }

    override val velocityContext: VelocityContext = VelocityContext()
    override var variables: SemanticVariables? = SemanticVariables(
        englishQuery = "",
        originLanguageQuery = "",
        hypotheticalDocument = "",
        relevantCode = listOf(),
    )

    fun updateQuery(query: ExplainQuery) {
        velocityContext.put("query", query)
    }

    override fun compile(input: String): String {
        // log.info("Compile input: {}", input)

        val sw = StringWriter()
        Velocity.evaluate(velocityContext, sw, "#" + this.javaClass.name, input)
        return sw.toString()
    }
}