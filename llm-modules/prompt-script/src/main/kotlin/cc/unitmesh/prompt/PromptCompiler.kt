package cc.unitmesh.prompt

interface PromptCompiler {
    fun compile(templatePath: String, dataPath: String): String
}

enum class PromptType {
    VELOCITY,
}