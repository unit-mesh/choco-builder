package cc.unitmesh.cf.core.context.variable

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter

class BasicVariableResolver : VariableResolver {
    override fun resolve(): String {
        TODO("Not yet implemented")
    }

    override fun compile(input: String): String {
        val stringBuilderWriter = StringWriter()
        val velocityContext = VelocityContext()

        // todo: build for resolver map
        val resolverMap = LinkedHashMap<String, String>(20)

        resolverMap.forEach { (key, value) ->
            velocityContext.put(key, value)
        }

        Velocity.evaluate(velocityContext, stringBuilderWriter, "", "")

        return stringBuilderWriter.toString()
    }
}