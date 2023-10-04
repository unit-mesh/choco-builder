package cc.unitmesh.prompt.template

interface PromptCompiler {
    fun compile(templatePath: String, dataPath: String): String
    fun compile(templatePath: String, dataPath: Map<String, Any>): String
}

