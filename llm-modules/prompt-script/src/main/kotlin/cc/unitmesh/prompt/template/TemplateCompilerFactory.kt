package cc.unitmesh.prompt.template


class TemplateCompilerFactory(private val type: TemplateEngineType = TemplateEngineType.VELOCITY) {
    private val compiler: PromptCompiler = when (type) {
        TemplateEngineType.VELOCITY -> {
            VelocityCompiler()
        }
    }

    fun compile(templatePath: String, dataPath: String): String {
        return compiler.compile(templatePath, dataPath)
    }
}