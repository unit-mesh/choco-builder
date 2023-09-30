package cc.unitmesh.prompt.template

/**
 * PromptCompiler is an interface for compile prompt template.
 */
interface PromptCompiler {
    fun compile(templatePath: String, dataPath: String): String
    fun compile(templatePath: String, dataPath: Map<String, Any>): String
}

