package cc.unitmesh.cf.core.context.variable

interface VariableResolver<T> {
    fun resolve(): T

    fun compile(input: String): String
}

