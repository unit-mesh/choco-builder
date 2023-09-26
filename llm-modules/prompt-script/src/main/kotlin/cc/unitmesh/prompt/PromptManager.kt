package cc.unitmesh.prompt

import cc.unitmesh.prompt.compiler.PromptCompiler
import cc.unitmesh.prompt.compiler.PromptType
import cc.unitmesh.prompt.compiler.VelocityCompiler


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