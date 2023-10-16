package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.project.GeniusProject
import cc.unitmesh.genius.prompt.PromptFactory

class CodeReviewPromptFactory: PromptFactory("prompts/review/code-review") {
    override fun createPrompt(project: GeniusProject, description: String): String {
        TODO("Not yet implemented")
    }
}
