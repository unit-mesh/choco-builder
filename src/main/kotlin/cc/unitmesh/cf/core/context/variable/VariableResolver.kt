package cc.unitmesh.cf.core.context.variable

interface VariableResolver {
    fun resolve(): String

    fun compile(input: String): String
}

