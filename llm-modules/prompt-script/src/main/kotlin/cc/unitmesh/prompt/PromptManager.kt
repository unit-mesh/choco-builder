package cc.unitmesh.prompt


class PromptManager(val type: PromptType = PromptType.VELOCITY) {
    private val compiler: PromptCompiler = when (type) {
        PromptType.VELOCITY -> {
            VelocityCompiler()
        }
    }

    fun compile(templatePath: String, dataPath: String): String {
        return compiler.compile(templatePath, dataPath)
    }
}