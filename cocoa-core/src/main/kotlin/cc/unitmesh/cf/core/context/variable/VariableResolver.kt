package cc.unitmesh.cf.core.context.variable

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter

interface VariableResolver<T> {
    var variables: T?

    val velocityContext: VelocityContext

    fun resolve(question: String = "") {}

    fun put(key: String, value: Any) {
        velocityContext.put(key, value)
    }

    fun get(key: String): Any? {
        return velocityContext.get(key)
    }

    fun compile(input: String): String {
        val sw = StringWriter()
        Velocity.evaluate(velocityContext, sw, "#" + this.javaClass.name, input)
        return sw.toString()
    }
}

