package cc.unitmesh.prompt

interface PromptCompiler {
    fun compile(templatePath: String, map: Map<String, Any>): String
}