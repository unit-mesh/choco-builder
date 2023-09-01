package cc.unitmesh.cf.core.context.variable

class BasicVariableResolver : VariableResolver<String> {
    override var variables: String? = ""

    override fun resolve(): String {
        TODO("Not yet implemented")
    }

    override fun compile(input: String): String {
        return ""
    }
}