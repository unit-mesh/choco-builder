package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.genius.prompt.PromptFactory
import cc.unitmesh.prompt.template.TemplateEngineType
import cc.unitmesh.prompt.template.VelocityCompiler

class CodeReviewPromptFactory : PromptFactory("code-review") {
    private val template = VelocityCompiler()

    override fun createPrompt(project: GeniusProject, description: String): String {
        val paths = listOf(
            "simple-review.open-ai.vm"
        )

        return loadPrompts(paths)
    }
}
