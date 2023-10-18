package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.genius.prompt.PromptFactory

class CodeReviewPromptFactory : PromptFactory("code-review") {
    override fun createPrompt(project: GeniusProject, description: String): String {
        val paths = listOf(
            "simple-review.open-ai.vm"
        )

        return loadPrompts(paths)
    }
}
