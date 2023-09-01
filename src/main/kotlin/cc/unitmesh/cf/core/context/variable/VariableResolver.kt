package cc.unitmesh.cf.core.context.variable

interface VariableResolver<T> {
    var variables: T?

    fun resolve(): T

    fun compile(input: String): String
}

