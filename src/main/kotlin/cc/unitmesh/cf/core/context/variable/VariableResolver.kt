package cc.unitmesh.cf.core.context.variable

import org.apache.velocity.VelocityContext

interface VariableResolver<T> {
    var variables: T?

    val velocityContext: VelocityContext

    fun resolve(question: String)

    fun put(key: String, value: Any) {
        velocityContext.put(key, value)
    }

    fun compile(input: String): String
}

