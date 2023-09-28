package cc.unitmesh.prompt

import cc.unitmesh.prompt.template.PromptCompiler
import cc.unitmesh.prompt.template.PromptType
import cc.unitmesh.prompt.template.VelocityCompiler


class TemplateCompilerFactory(private val type: PromptType = PromptType.VELOCITY) {
    private val compiler: PromptCompiler = when (type) {
        PromptType.VELOCITY -> {
            VelocityCompiler()
        }
    }

    fun compile(templatePath: String, dataPath: String): String {
        return compiler.compile(templatePath, dataPath)
    }
}