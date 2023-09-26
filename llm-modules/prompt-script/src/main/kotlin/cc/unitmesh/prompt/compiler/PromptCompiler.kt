package cc.unitmesh.prompt.compiler

interface PromptCompiler {
    fun compile(templatePath: String, dataPath: String): String
}

enum class PromptType {
    VELOCITY,
}