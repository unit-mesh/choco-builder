package cc.unitmesh.genius.domain.review

import cc.unitmesh.genius.project.SimpleProject
import cc.unitmesh.genius.prompt.PromptFactory

class CodeReviewPromptFactory: PromptFactory("prompts/review/code-review") {
    override fun createPrompt(project: SimpleProject, description: String): String {
        TODO("Not yet implemented")
    }
}
